package com.blockbyblockwest.fest.proxylink.command;

import com.blockbyblockwest.fest.proxylink.ProxyLinkVelocity;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BroadcastCommand implements Command {

  private static final char COLOR_CHAR = '\u00A7';

  private final ProxyLinkVelocity plugin;

  public BroadcastCommand(ProxyLinkVelocity plugin) {
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSource source, @NonNull String[] args) {
    if (args.length > 0) {
      try {
        plugin.getNetworkService().broadcast(String.join(" ", args).replace('&', COLOR_CHAR));
      } catch (ServiceException e) {
        e.printStackTrace();
        source.sendMessage(TextComponent.of("An error occurred", TextColor.RED));
      }
    }
  }

  @Override
  public boolean hasPermission(CommandSource source, @NonNull String[] args) {
    return source.hasPermission("proxylink.broadcast");
  }

}
