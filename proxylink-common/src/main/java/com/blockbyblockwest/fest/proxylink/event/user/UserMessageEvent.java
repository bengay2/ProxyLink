package com.blockbyblockwest.fest.proxylink.event.user;

import com.blockbyblockwest.fest.proxylink.user.MessageType;
import java.util.UUID;

public final class UserMessageEvent {

  private final UUID uniqueId;
  private final String message;
  private final MessageType type;

  public UserMessageEvent(UUID uniqueId, String message, MessageType type) {
    this.uniqueId = uniqueId;
    this.message = message;
    this.type = type;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getMessage() {
    return message;
  }

  public MessageType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "UserMessageEvent{" +
        "uniqueId=" + uniqueId +
        ", message='" + message + '\'' +
        ", type=" + type +
        '}';
  }
}
