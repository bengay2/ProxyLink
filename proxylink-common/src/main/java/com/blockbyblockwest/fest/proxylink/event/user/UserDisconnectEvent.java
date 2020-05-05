package com.blockbyblockwest.fest.proxylink.event.user;

import java.util.UUID;

public final class UserDisconnectEvent {

  private final UUID uniqueId;

  public UserDisconnectEvent(UUID uniqueId) {
    this.uniqueId = uniqueId;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  @Override
  public String toString() {
    return "UserDisconnectEvent{" +
        "uniqueId=" + uniqueId +
        '}';
  }

}
