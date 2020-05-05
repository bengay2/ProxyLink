package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.user.UserMessageEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;
import com.blockbyblockwest.fest.proxylink.user.MessageType;
import java.util.UUID;

public class UserMessagePacket extends PubSubPacket {

  private UUID uniqueId;
  private String message;
  private MessageType type;

  public UserMessagePacket(UUID uniqueId, String message, MessageType type) {
    this.uniqueId = uniqueId;
    this.message = message;
    this.type = type;
  }

  public UserMessagePacket() {
  }

  @Override
  public void read(String msg) {
    String[] data = ESCAPING_COLON.split(msg);

    uniqueId = UUID.fromString(data[0]);
    type = MessageType.valueOf(data[1]);
    message = reverseEscaping(data[2]);

  }

  @Override
  public String toPacket() {
    return uniqueId.toString() + ":" + type.name() + ":" + escapingString(message);
  }

  @Override
  public String getChannel() {
    return NetworkKey.USER_MESSAGE;
  }

  @Override
  public Object toEvent() {
    return new UserMessageEvent(uniqueId, message, type);
  }

}
