package com.blockbyblockwest.fest.proxylink.models;

import com.blockbyblockwest.fest.proxylink.ServerType;

public interface BackendServer extends Server {

  ServerType getServerType();

  String getHost();

  int getPort();

  int getPlayerCount();

  int getMaxPlayerCount();

  default boolean isFull() {
    return getPlayerCount() >= getMaxPlayerCount();
  }

}
