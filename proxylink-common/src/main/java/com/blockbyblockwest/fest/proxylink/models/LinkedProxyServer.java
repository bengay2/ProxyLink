package com.blockbyblockwest.fest.proxylink.models;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import java.util.Set;
import java.util.UUID;

public interface LinkedProxyServer extends Server {

  int HARD_PLAYER_LIMIT = 3000;

  default int getMaxPlayerCount() {
    return HARD_PLAYER_LIMIT;
  }
  
  Set<UUID> getOnlineUsers() throws ServiceException;

}
