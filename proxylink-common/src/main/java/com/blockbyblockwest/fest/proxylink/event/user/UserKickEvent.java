package com.blockbyblockwest.fest.proxylink.event.user;

import java.util.UUID;

public class UserKickEvent {

  private final UUID uniqueId;
  private final String reason;

  public UserKickEvent(UUID uniqueId, String reason) {
    this.uniqueId = uniqueId;
    this.reason = reason;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getReason() {
    return reason;
  }

  @Override
  public String toString() {
    return "UserKickEvent{" +
        "uniqueId=" + uniqueId +
        '}';
  }

}
