package com.blockbyblockwest.fest.proxylink.event;

import com.blockbyblockwest.fest.proxylink.EventExecutor;
import org.bukkit.plugin.PluginManager;

public class BukkitEventExecutor implements EventExecutor {

  private final PluginManager pluginManager;

  public BukkitEventExecutor(PluginManager pluginManager) {
    this.pluginManager = pluginManager;
  }

  @Override
  public void postEvent(Object event) {
    pluginManager.callEvent(new ProxyLinkEvent(event));
  }

}
