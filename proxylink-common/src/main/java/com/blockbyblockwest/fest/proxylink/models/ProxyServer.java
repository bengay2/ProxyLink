package com.blockbyblockwest.fest.proxylink.models;

import java.util.Set;
import java.util.UUID;

public interface ProxyServer extends Server {

  int getMaxPlayerCount();
  
  Set<UUID> getOnlineUsers();

}
