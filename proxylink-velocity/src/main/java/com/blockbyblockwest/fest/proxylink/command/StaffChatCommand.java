package com.blockbyblockwest.fest.proxylink.command;

import com.blockbyblockwest.fest.proxylink.ProxyLinkVelocity;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import java.util.Optional;
import java.util.StringJoiner;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class StaffChatCommand implements Command {

  private final static String PERMISSION = "proxylink.staffchat";

  // Can't say I'm a fan but.. oh well
  private static final char COLOR_CHAR = '\u00A7';
  private final static String SERVER_NAME_FORMAT = "&8[&a%s&8]".replace('&', COLOR_CHAR);
  private final static String FULL_MESSAGE_FORMAT = "&8[&2&lSC&r&8] %s &a%s: %s"
      .replace('&', COLOR_CHAR);
  private final ProxyLinkVelocity plugin;

  public StaffChatCommand(ProxyLinkVelocity plugin) {
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSource sender, @NonNull String[] args) {
    if (args.length < 1) {
      sender.sendMessage(TextComponent.of("Usage: /staffchat <message>").color(TextColor.RED));
      return;
    }

    StringJoiner joiner = new StringJoiner(" ");
    for (String arg : args) {
      joiner.add(arg);
    }

    String username;
    String server;

    if (sender instanceof Player) {
      Player player = (Player) sender;
      username = player.getUsername();
      Optional<ServerConnection> serverConnection = player.getCurrentServer();
      server = serverConnection.map(
          connection -> String.format(SERVER_NAME_FORMAT, connection.getServerInfo().getName()))
          .orElse(" ");
    } else {
      username = "Console";
      server = " ";
    }

    try {
      plugin.getNetworkService()
          .broadcast(String.format(FULL_MESSAGE_FORMAT, server, username, joiner.toString()),
              PERMISSION);
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public boolean hasPermission(CommandSource source, @NonNull String[] args) {
    return source.hasPermission(PERMISSION);
  }

}
