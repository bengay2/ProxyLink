package com.blockbyblockwest.fest.proxylink.listener;

import com.blockbyblockwest.fest.proxylink.NetworkService;
import com.blockbyblockwest.fest.proxylink.ProxyLinkVelocity;
import com.blockbyblockwest.fest.proxylink.event.backendserver.BackendServerRegisterEvent;
import com.blockbyblockwest.fest.proxylink.event.backendserver.BackendServerUnregisterEvent;
import com.blockbyblockwest.fest.proxylink.event.user.UserKickEvent;
import com.blockbyblockwest.fest.proxylink.event.user.UserMessageEvent;
import com.blockbyblockwest.fest.proxylink.event.user.UserSwitchServerRequestEvent;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.models.BackendServer;
import com.blockbyblockwest.fest.proxylink.models.LinkedProxyServer;
import com.blockbyblockwest.fest.proxylink.models.NetworkPingData;
import com.blockbyblockwest.fest.proxylink.user.MessageType;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent.ComponentResult;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;
import com.velocitypowered.api.proxy.server.ServerPing.Version;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class ProxyLinkListener {

  private final ProxyLinkVelocity plugin;
  private final NetworkService networkService;
  private final Set<UUID> playerInBackend = Collections.newSetFromMap(new ConcurrentHashMap<>());
  private final ServerPingCache pingCache = new ServerPingCache();

  public ProxyLinkListener(ProxyLinkVelocity plugin, NetworkService networkService) {
    this.plugin = plugin;
    this.networkService = networkService;
  }

  @Subscribe(order = PostOrder.LATE)
  public void onLogin(LoginEvent e) {
    // Account for other plugins, making the case more simple
    if (!e.getResult().isAllowed()) {
      return;
    }

    if (plugin.getProxy().getPlayerCount() >= LinkedProxyServer.HARD_PLAYER_LIMIT) {
      e.setResult(ComponentResult.denied(TextComponent.of("Proxy is full", TextColor.RED)));
      return;
    }

    try {
      if (networkService.isUserOnline(e.getPlayer().getUniqueId())) {
        e.setResult(ComponentResult.denied(TextComponent.of("Already connected.", TextColor.RED)));
      } else {
        networkService.connectUserToProxy(e.getPlayer().getUniqueId(), plugin.getServerId());
        playerInBackend.add(e.getPlayer().getUniqueId());
        plugin.getLogger().info("Connected {} to network service", e.getPlayer().getUniqueId());
      }
    } catch (ServiceException ex) {
      ex.printStackTrace();
      e.setResult(ComponentResult.denied(TextComponent.of("An error occurred.", TextColor.RED)));
    }

  }

  @Subscribe
  public void onDisconnect(DisconnectEvent e) {
    if (playerInBackend.remove(e.getPlayer().getUniqueId())) {
      disconnectUntilSuccess(e.getPlayer().getUniqueId(), 0);
    }
  }

  /*
   * Quite hacky, but with that we can survive downtime of Redis
   */
  private void disconnectUntilSuccess(UUID userId, int retryCount) {
    try {
      if (networkService.isUserOnline(userId)) {
        networkService.disconnectUser(userId, plugin.getServerId());
        plugin.getLogger().info("Disconnected {} from network service", userId);
      } else {
        plugin.getLogger().error("{} was not connected to the network", userId);
      }
    } catch (ServiceException ex) {
      if (retryCount == 0) {
        ex.printStackTrace();
      } else {
        plugin.getLogger().error("Unable to disconnect {}. Cause: {}", userId, ex.getMessage());
      }
      plugin.getProxy().getScheduler()
          .buildTask(plugin, () -> disconnectUntilSuccess(userId, retryCount + 1))
          .delay(5, TimeUnit.SECONDS).schedule();
    }
  }

  @Subscribe
  public void onServerConnected(ServerConnectedEvent e) {
    setServerUntilSuccess(e.getPlayer().getUniqueId(), e.getServer().getServerInfo().getName(), 0);
  }

  /*
   * Again hacky to bridge a Redis restart
   */
  private void setServerUntilSuccess(UUID userId, String toServer, int retryCount) {
    try {
      networkService.switchServer(userId, toServer);
    } catch (ServiceException ex) {
      if (retryCount == 0) {
        ex.printStackTrace();
      } else {
        plugin.getLogger().error("Unable to disconnect {}. Cause: {}", userId, ex.getMessage());
      }
      plugin.getProxy().getScheduler()
          .buildTask(plugin, () -> setServerUntilSuccess(userId, toServer, retryCount + 1))
          .delay(5, TimeUnit.SECONDS).schedule();
    }
  }

  @Subscribe
  public void on(ProxyPingEvent e) {
    try {
      e.setPing(pingCache.getServerPing(e.getPing()));
    } catch (ServiceException ex) {
      ex.printStackTrace();
      e.setPing(e.getPing().asBuilder()
          .description(TextComponent.of("Failed to process your ping"))
          .onlinePlayers(0)
          .maximumPlayers(0)
          .build());
    }
  }

  private class ServerPingCache {

    private Instant lastUpdate = Instant.EPOCH;
    private Component description = TextComponent.of("Dummy");
    private SamplePlayer[] hoverText = new SamplePlayer[0];
    private int cachedLimit;
    private int cachedOnline;

    public ServerPing getServerPing(ServerPing original) throws ServiceException {
      if (Duration.between(lastUpdate, Instant.now()).toMillis() < 1000) { // one second bounce off
        return buildFromData(original);
      }

      NetworkPingData pingData = networkService.getPingData();
      description = TextComponent.of(pingData.getDescription());
      hoverText = pingData.getHoverMessage().stream()
          .map(line -> new SamplePlayer(line, UUID.randomUUID()))
          .toArray(SamplePlayer[]::new);

      cachedOnline = networkService.getOnlineUserCount();
      cachedLimit = networkService.getMaxPlayerCount();

      lastUpdate = Instant.now();

      return buildFromData(original);
    }

    public ServerPing buildFromData(ServerPing original) {
      return original.asBuilder()
          .description(description)
          .samplePlayers(hoverText)
          .maximumPlayers(cachedLimit)
          .onlinePlayers(cachedOnline)
          .version(new Version(original.getVersion().getProtocol(), "BXBW 1.8-1.15"))
          .build();
    }
  }

  // Processing remote events from here

  @Subscribe
  public void onServerRegister(BackendServerRegisterEvent e) {
    toVelocityServer(e.getBackendServer())
        .ifPresent(found -> plugin.getProxy().unregisterServer(found.getServerInfo()));

    plugin.getProxy().registerServer(new ServerInfo(e.getBackendServer().getId(),
        new InetSocketAddress(e.getBackendServer().getHost(), e.getBackendServer().getPort())));
  }

  @Subscribe
  public void onServerUnregister(BackendServerUnregisterEvent e) {
    plugin.getProxy().getServer(e.getServerId())
        .ifPresent(found -> plugin.getProxy().unregisterServer(found.getServerInfo()));

  }

  @Subscribe
  public void onSwitchRequest(UserSwitchServerRequestEvent e) {
    plugin.getProxy().getServer(e.getToServer())
        .ifPresent(server -> plugin.getProxy().getPlayer(e.getUniqueId())
            .ifPresent(player -> player.createConnectionRequest(server).fireAndForget()));

  }

  private Optional<RegisteredServer> toVelocityServer(BackendServer backendServer) {
    return plugin.getProxy().getServer(backendServer.getId());
  }

  @Subscribe
  public void onUserMessage(UserMessageEvent e) {
    if (e.getType() == MessageType.COMPONENT) {
      plugin.getProxy().getPlayer(e.getUniqueId())
          .ifPresent(player -> player
              .sendMessage(GsonComponentSerializer.INSTANCE.deserialize(e.getMessage())));
    } else if (e.getType() == MessageType.STRING) {
      plugin.getProxy().getPlayer(e.getUniqueId())
          .ifPresent(player -> player
              .sendMessage(LegacyComponentSerializer.legacy().deserialize(e.getMessage())));
    }
  }

  @Subscribe
  public void onUserKick(UserKickEvent e) {
    plugin.getProxy().getPlayer(e.getUniqueId())
        .ifPresent(player -> player
            .disconnect(LegacyComponentSerializer.legacy().deserialize(e.getReason())));

  }

}