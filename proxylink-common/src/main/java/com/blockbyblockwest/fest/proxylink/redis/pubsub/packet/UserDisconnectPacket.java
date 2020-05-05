package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.user.UserDisconnectEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;
import java.util.UUID;

public class UserDisconnectPacket extends PubSubPacket {

  private UUID uniqueId;

  public UserDisconnectPacket() {
  }

  public UserDisconnectPacket(UUID uniqueId) {
    this.uniqueId = uniqueId;
  }

  @Override
  public void read(String msg) {
    uniqueId = UUID.fromString(msg);
  }

  @Override
  public String toPacket() {
    return uniqueId.toString();
  }

  @Override
  public String getChannel() {
    return NetworkKey.USER_DISCONNECT;
  }

  @Override
  public Object toEvent() {
    return new UserDisconnectEvent(uniqueId);
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

}
