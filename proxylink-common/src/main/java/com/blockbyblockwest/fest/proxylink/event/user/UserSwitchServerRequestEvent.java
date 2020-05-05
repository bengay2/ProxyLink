package com.blockbyblockwest.fest.proxylink.event.user;

import java.util.UUID;

public final class UserSwitchServerRequestEvent {

  private final UUID uniqueId;
  private final String toServer;

  public UserSwitchServerRequestEvent(UUID uniqueId, String toServer) {
    this.uniqueId = uniqueId;
    this.toServer = toServer;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getToServer() {
    return toServer;
  }

  @Override
  public String toString() {
    return "UserSwitchServerRequestEvent{" +
        "uniqueId=" + uniqueId +
        ", toServer='" + toServer + '\'' +
        '}';
  }

}
