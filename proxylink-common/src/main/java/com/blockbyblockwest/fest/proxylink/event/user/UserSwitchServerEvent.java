package com.blockbyblockwest.fest.proxylink.event.user;

import java.util.UUID;

public class UserSwitchServerEvent {

  private final UUID uniqueId;
  private final String newServerId;

  public UserSwitchServerEvent(UUID uniqueId, String newServerId) {
    this.uniqueId = uniqueId;
    this.newServerId = newServerId;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getNewServerId() {
    return newServerId;
  }

}
