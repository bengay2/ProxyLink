package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.user.UserKickEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;
import java.util.UUID;

public class UserKickPacket extends PubSubPacket {

  private UUID uniqueId;
  private String reason;

  public UserKickPacket() {
  }

  public UserKickPacket(UUID uniqueId, String reason) {
    this.uniqueId = uniqueId;
    this.reason = reason;
  }

  @Override
  public void read(String msg) {
    String[] split = ESCAPING_COLON.split(msg);

    uniqueId = UUID.fromString(split[0]);
    reason = reverseEscaping(split[1]);

  }

  @Override
  public String toPacket() {
    return uniqueId.toString() + ":" + escapingString(reason);
  }

  @Override
  public String getChannel() {
    return NetworkKey.USER_KICK;
  }

  @Override
  public Object toEvent() {
    return new UserKickEvent(uniqueId, reason);
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

  public String getReason() {
    return this.reason;
  }

}
