package com.blockbyblockwest.fest.proxylink;

import com.blockbyblockwest.fest.proxylink.event.BukkitEventExecutor;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.profile.ProfileService;
import com.blockbyblockwest.fest.proxylink.redis.Credentials;
import com.blockbyblockwest.fest.proxylink.redis.RedisBackend;
import com.blockbyblockwest.fest.proxylink.redis.RedisNetworkService;
import com.blockbyblockwest.fest.proxylink.redis.profile.RedisProfileService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ProxyLinkBukkit extends JavaPlugin {

  private static ProxyLinkBukkit instance;

  public static ProxyLinkBukkit getInstance() {
    return instance;
  }

  private final RedisBackend redisBackend = new RedisBackend();
  private final ScheduledExecutorService heartbeatExecutor = Executors
      .newSingleThreadScheduledExecutor();

  private ScheduledFuture<?> heartbeatFuture;
  private ScheduledFuture<?> playercountUpdateFuture;

  private NetworkService networkService;
  private ProfileService profileService;
  private String serverId;
  private ServerType serverType;

  private NetworkPlayerCount networkPlayerCount;

  @Override
  public void onEnable() {
    instance = this;

    saveDefaultConfig();

    try {
      redisBackend.initialize(new Credentials(getConfig().getString("redis.host"),
          getConfig().getString("redis.password"), getConfig().getInt("redis.database"),
          getConfig().getInt("redis.port"), getConfig().getBoolean("redis.ssl")));

      networkService = new RedisNetworkService(redisBackend.getJedisPool(),
          new BukkitEventExecutor(getServer().getPluginManager()));
      networkService.disableHighFrequencyPackets();
      networkService.initialize();

      profileService = new RedisProfileService(redisBackend.getJedisPool());

      serverId = getConfig().getString("serverid");
      try {
        serverType = ServerType.valueOf(getConfig().getString("servertype"));

        networkService.registerServer(serverId, serverType,
            InetAddress.getLocalHost().getHostAddress(), Bukkit.getPort(), Bukkit.getMaxPlayers());

        heartbeatFuture = heartbeatExecutor.scheduleAtFixedRate(() -> {
          try {
            networkService.serverHeartBeat(serverId, Bukkit.getOnlinePlayers().size());
          } catch (ServiceException e) {
            e.printStackTrace();
          }
        }, 0, 2, TimeUnit.SECONDS);

        networkPlayerCount = new NetworkPlayerCount(networkService);

        playercountUpdateFuture = heartbeatExecutor
            .scheduleAtFixedRate(() -> networkPlayerCount.update(), 0, 4, TimeUnit.SECONDS);
      } catch (IllegalArgumentException | UnknownHostException ex) {
        throw new ServiceException("Invalid config");
      }
    } catch (ServiceException e) {
      e.printStackTrace();
      Bukkit.shutdown(); // If we encounter a ServiceException here, this server is useless
    }
  }

  @Override
  public void onDisable() {
    if (heartbeatFuture != null) {
      heartbeatFuture.cancel(true);
    }
    if (playercountUpdateFuture != null) {
      playercountUpdateFuture.cancel(true);
    }
    heartbeatExecutor.shutdown();
    try {
      if (!heartbeatExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
        getLogger().severe("Timed out shutting down heartbeat thread");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    if (networkService != null) {
      try {
        networkService.removeServer(serverId);
      } catch (ServiceException e) {
        e.printStackTrace();
      }
      networkService.shutdown();
    }
    redisBackend.shutdown();
  }

  public NetworkService getNetworkService() {
    return networkService;
  }

  public String getServerId() {
    return serverId;
  }

  public ServerType getServerType() {
    return serverType;
  }

  public ProfileService getProfileService() {
    return profileService;
  }

  public NetworkPlayerCount getNetworkPlayerCount() {
    return networkPlayerCount;
  }

}
