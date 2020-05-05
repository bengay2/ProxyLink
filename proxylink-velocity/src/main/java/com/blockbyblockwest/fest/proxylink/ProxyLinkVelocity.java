package com.blockbyblockwest.fest.proxylink;

import com.blockbyblockwest.fest.proxylink.config.Config;
import com.blockbyblockwest.fest.proxylink.event.VelocityEventExecutor;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.listener.ProfileUpdateListener;
import com.blockbyblockwest.fest.proxylink.listener.ProxyLinkListener;
import com.blockbyblockwest.fest.proxylink.profile.ProfileService;
import com.blockbyblockwest.fest.proxylink.redis.Credentials;
import com.blockbyblockwest.fest.proxylink.redis.RedisBackend;
import com.blockbyblockwest.fest.proxylink.redis.RedisNetworkService;
import com.blockbyblockwest.fest.proxylink.redis.profile.RedisProfileService;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;

@Plugin(id = "proxylink", name = "ProxyLink", version = "1.0", authors = {"Gabik21"})
public class ProxyLinkVelocity {

  private static ProxyLinkVelocity instance;

  public static ProxyLinkVelocity getInstance() {
    return instance;
  }

  private final RedisBackend redisBackend = new RedisBackend();

  private final ProxyServer proxy;
  private final CommandManager commandManager;
  private final Path directory;
  private final Logger logger;

  private Config config;
  private String serverId;

  private NetworkService networkService;
  private ProfileService profileService;

  @Inject
  public ProxyLinkVelocity(ProxyServer proxy, CommandManager commandManager,
      @DataDirectory Path directory, Logger logger) {
    this.proxy = proxy;
    this.commandManager = commandManager;
    this.directory = directory;
    this.logger = logger;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    instance = this;

    File configFile = new File(directory.toFile(), "config.conf");
    this.config = new Config(configFile);
    try {
      this.config.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.serverId = config.getNode("serverid").getString();
    try {
      ConfigurationNode redisNode = config.getNode("redis");
      redisBackend.initialize(new Credentials(redisNode.getNode("host").getString(),
          redisNode.getNode("password").getString(), redisNode.getNode("database").getInt(),
          redisNode.getNode("port").getInt()));

      networkService = new RedisNetworkService(redisBackend.getJedisPool(),
          new VelocityEventExecutor(proxy.getEventManager()));
      profileService = new RedisProfileService(redisBackend.getJedisPool());

      networkService.removeProxy(serverId);
      networkService.proxyHeartBeat(serverId);
    } catch (ServiceException e) {
      e.printStackTrace();
    }

    proxy.getEventManager().register(this, new ProxyLinkListener(this, networkService));
    proxy.getEventManager().register(this, new ProfileUpdateListener(profileService));

  }

  @Subscribe
  public void onShutdown(ProxyShutdownEvent e) {
    networkService.shutdown();
    redisBackend.shutdown();
  }

  public NetworkService getNetworkService() {
    return networkService;
  }

  public ProfileService getProfileService() {
    return profileService;
  }

  public String getServerId() {
    return serverId;
  }

  public ProxyServer getProxy() {
    return proxy;
  }

  public Logger getLogger() {
    return logger;
  }

}
