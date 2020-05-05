package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.user.UserSwitchServerEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;
import java.util.UUID;

public class UserSwitchServerPacket extends PubSubPacket {

  private UUID uniqueId;
  private String toServer;

  public UserSwitchServerPacket() {
  }

  public UserSwitchServerPacket(UUID uniqueId, String toServer) {
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
    return uniqueId + ":" + toServer;
  }

  @Override
  public String getChannel() {
    return NetworkKey.USER_SWITCH_SERVER;
  }

  @Override
  public Object toEvent() {
    return new UserSwitchServerEvent(uniqueId, toServer);
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

  public String getToServer() {
    return this.toServer;
  }

}
