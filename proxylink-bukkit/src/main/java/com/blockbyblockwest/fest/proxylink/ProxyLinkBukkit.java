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
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ProxyLinkBukkit extends JavaPlugin {

  private static ProxyLinkBukkit instance;

  public static ProxyLinkBukkit getInstance() {
    return instance;
  }

  private final RedisBackend redisBackend = new RedisBackend();
  private NetworkService networkService;
  private ProfileService profileService;
  private String serverId;
  private BukkitTask heartbeatTask;

  @Override
  public void onEnable() {
    instance = this;

    try {
      redisBackend.initialize(new Credentials(getConfig().getString("redis.host"),
          getConfig().getString("redis.password"), getConfig().getInt("redis.database"),
          getConfig().getInt("redis.port")));

      networkService = new RedisNetworkService(redisBackend.getJedisPool(),
          new BukkitEventExecutor(getServer().getPluginManager()));
      networkService.initialize();

      profileService = new RedisProfileService(redisBackend.getJedisPool());

      serverId = getConfig().getString("serverid");
      try {

        networkService.registerServer(serverId,
            ServerType.valueOf(getConfig().getString("servertype")),
            InetAddress.getLocalHost().getHostAddress(), Bukkit.getPort(), Bukkit.getMaxPlayers());

        heartbeatTask = new BukkitRunnable() {
          @Override
          public void run() {
            try {
              networkService.serverHeartBeat(serverId);
            } catch (ServiceException e) {
              e.printStackTrace();
            }
          }
        }.runTaskTimerAsynchronously(this, 0, 20 * 20);

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
    if (heartbeatTask != null) {
      heartbeatTask.cancel();
    }
    if (networkService != null) {
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

  public ProfileService getProfileService() {
    return profileService;
  }

}
