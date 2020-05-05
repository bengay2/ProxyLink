package com.blockbyblockwest.fest.proxylink.redis.pubsub;

import com.blockbyblockwest.fest.proxylink.redis.models.RedisBackendServer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LocalNetworkState {

  private final Map<UUID, String> userServerMap = new ConcurrentHashMap<>();
  private final Map<String, RedisBackendServer> serverInfo = new ConcurrentHashMap<>();

  public void invalidate() {
    userServerMap.clear();
    serverInfo.clear();
  }

  public String getServerByUser(UUID uniqueId) {
    return userServerMap.get(uniqueId);
  }

  public RedisBackendServer getServerInfo(String id) {
    return serverInfo.get(id);
  }

  public Map<String, RedisBackendServer> getServerInfo() {
    return serverInfo;
  }

  public Map<UUID, String> getUserServerMap() {
    return userServerMap;
  }

}
