package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.user.UserSwitchServerRequestEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;
import java.util.UUID;

public class UserSwitchServerRequestPacket extends PubSubPacket {

  private UUID uniqueId;
  private String toServer;

  public UserSwitchServerRequestPacket() {
  }

  public UserSwitchServerRequestPacket(UUID uniqueId, String toServer) {
    this.uniqueId = uniqueId;
    this.toServer = toServer;
  }

  @Override
  public void read(String msg) {
    String[] split = ESCAPING_COLON.split(msg);
    uniqueId = UUID.fromString(split[0]);
    toServer = split[1];
  }

  @Override
  public String toPacket() {
    return uniqueId.toString() + ":" + toServer;
  }

  @Override
  public String getChannel() {
    return NetworkKey.USER_SWITCH_SERVER_REQUEST;
  }

  @Override
  public Object toEvent() {
    return new UserSwitchServerRequestEvent(uniqueId, toServer);
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

  public String getToServer() {
    return this.toServer;
  }

}
