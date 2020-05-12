package com.blockbyblockwest.fest.proxylink;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkPlayerCount {

  private final AtomicInteger playerCount = new AtomicInteger();
  private final NetworkService networkService;

  public NetworkPlayerCount(NetworkService networkService) {
    this.networkService = networkService;
  }

  public void update() {
    try {
      playerCount.set(networkService.getOnlineUserCount());
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public int getPlayerCount() {
    return playerCount.get();
  }

}
