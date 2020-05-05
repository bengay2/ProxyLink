package com.blockbyblockwest.fest.proxylink.redis.models;

import com.blockbyblockwest.fest.proxylink.ServerType;
import com.blockbyblockwest.fest.proxylink.models.BackendServer;

public class RedisBackendServer extends RedisServer implements BackendServer {

  private final ServerType serverType;
  private final String host;
  private final int port;
  private final int maxPlayerCount;

  private int playerCount = -1;

  public RedisBackendServer(String id, ServerType serverType, String host, int port,
      int maxPlayerCount) {
    super(id);
    this.serverType = serverType;
    this.host = host;
    this.port = port;
    this.maxPlayerCount = maxPlayerCount;
  }

  @Override
  public ServerType getServerType() {
    return serverType;
  }

  @Override
  public String getHost() {
    return host;
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public int getMaxPlayerCount() {
    return maxPlayerCount;
  }

  @Override
  public int getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

}
