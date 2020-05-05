package com.blockbyblockwest.fest.proxylink.event.backendserver;

public class BackendServerUpdatePlayerCountEvent {

  private final String serverId;
  private final int playerCount;

  public BackendServerUpdatePlayerCountEvent(String serverId, int playerCount) {
    this.serverId = serverId;
    this.playerCount = playerCount;
  }

  public String getServerId() {
    return serverId;
  }

  public int getPlayerCount() {
    return playerCount;
  }

}
