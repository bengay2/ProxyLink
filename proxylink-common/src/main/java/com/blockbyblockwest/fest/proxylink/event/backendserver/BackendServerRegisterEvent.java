package com.blockbyblockwest.fest.proxylink.event.backendserver;

import com.blockbyblockwest.fest.proxylink.models.BackendServer;

public final class BackendServerRegisterEvent {

  private final BackendServer backendServer;

  public BackendServerRegisterEvent(BackendServer backendServer) {
    this.backendServer = backendServer;
  }

  public BackendServer getBackendServer() {
    return backendServer;
  }

  @Override
  public String toString() {
    return "BackendServerRegisterEvent{" +
        "backendServer=" + backendServer +
        '}';
  }

}
