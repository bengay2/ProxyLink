package com.blockbyblockwest.fest.proxylink.command;

import com.blockbyblockwest.fest.proxylink.ProxyLinkVelocity;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class SendCommand implements Command {

  private final ProxyLinkVelocity plugin;

  public SendCommand(ProxyLinkVelocity plugin) {
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSource source, @NonNull String[] args) {
    try {
      plugin.getProfileService().getProfile(args[0]).ifPresent(profile -> {
        try {
          if (plugin.getNetworkService().isUserOnline(profile.getUniqueId())) {
            RegisteredServer targetServer = null;
            if (args.length > 1) {
              targetServer = plugin.getProxy().getServer(args[1]).orElse(null);
            } else if (source instanceof Player) {
              targetServer = ((Player) source).getCurrentServer().map(ServerConnection::getServer)
                  .orElse(null);
            }
            if (targetServer != null) {
              source.sendMessage(TextComponent.of("Trying to send them over...", TextColor.GREEN));
              plugin.getNetworkService().getUser(profile.getUniqueId())
                  .sendToServer(targetServer.getServerInfo().getName());
            } else {
              source.sendMessage(TextComponent.of("No target server found", TextColor.RED));
            }
          } else {
            source.sendMessage(TextComponent.of("Player is not online", TextColor.RED));
          }
        } catch (ServiceException e) {
          e.printStackTrace();
          source.sendMessage(TextComponent.of("An error occurred", TextColor.RED));
        }
      });
    } catch (ServiceException e) {
      e.printStackTrace();
      source.sendMessage(TextComponent.of("An error occurred", TextColor.RED));
    }
  }

  @Override
  public List<String> suggest(CommandSource source, @NonNull String[] currentArgs) {
    if (currentArgs.length != 1 || currentArgs[0].length() < 2) {
      return Collections.emptyList();
    }
    return plugin.getOnlinePlayerNames().getNames()
        .stream()
        .filter(name -> name.regionMatches(true, 0, currentArgs[0], 0, currentArgs[0].length()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean hasPermission(CommandSource source, @NonNull String[] args) {
    return source.hasPermission("proxylink.sendto");
  }

}
