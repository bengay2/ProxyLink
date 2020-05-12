package com.blockbyblockwest.fest.proxylink.command;

import com.blockbyblockwest.fest.proxylink.ProxyLinkVelocity;
import com.blockbyblockwest.fest.proxylink.ServerType;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.models.BackendServer;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import java.util.Comparator;
import java.util.Optional;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class HubCommand implements Command {

  private final ProxyLinkVelocity plugin;

  public HubCommand(ProxyLinkVelocity plugin) {
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSource source, @NonNull String[] strings) {
    if (source instanceof Player) {
      Player player = (Player) source;
      player.getCurrentServer().ifPresent(connection -> {
        try {
          plugin.getNetworkService().getServer(connection.getServerInfo().getName()).ifPresent(
              backendServer -> {
                if (backendServer.getServerType() == ServerType.HUB) {
                  player.sendMessage(
                      TextComponent.of("You are already connected to a hub!").color(TextColor.RED));
                  return;
                }

                try {
                  Optional<? extends BackendServer> hubServer = plugin.getNetworkService()
                      .getServers().stream()
                      .filter(server -> server.getServerType() == ServerType.HUB)
                      .min(Comparator.comparingInt(BackendServer::getPlayerCount));

                  if (hubServer.isPresent()) {
                    player.sendMessage(
                        TextComponent
                            .of("Connecting you to a hub.. (" + hubServer.get().getId() + ")")
                            .color(TextColor.GREEN));
                    plugin.getProxy().getServer(hubServer.get().getId()).ifPresent(
                        registeredServer -> player.createConnectionRequest(registeredServer)
                            .fireAndForget());
                  } else {
                    player.sendMessage(
                        TextComponent.of("There are no hub servers available!")
                            .color(TextColor.RED));
                  }

                } catch (ServiceException e) {
                  e.printStackTrace();
                }

              });
        } catch (ServiceException e) {
          e.printStackTrace();
        }
      });

    }
  }

}
