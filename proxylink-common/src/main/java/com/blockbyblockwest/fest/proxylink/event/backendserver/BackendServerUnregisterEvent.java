package com.blockbyblockwest.fest.proxylink.event.backendserver;

public final class BackendServerUnregisterEvent {

  private final String serverId;

  public BackendServerUnregisterEvent(String serverId) {
    this.serverId = serverId;
  }

  public String getServerId() {
    return serverId;
  }

  @Override
  public String toString() {
    return "BackendServerUnregisterEvent{" +
        "serverId='" + serverId + '\'' +
        '}';
  }

}
