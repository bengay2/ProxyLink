package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.NetworkBroadcastEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;

public class NetworkBroadcastPacket extends PubSubPacket {

  private String message;
  private String permission;

  public NetworkBroadcastPacket(String message, String permission) {
    this.message = message;
    this.permission = permission;
  }

  public NetworkBroadcastPacket() {
  }

  @Override
  public void read(String msg) {
    String[] split = ESCAPING_COLON.split(msg);
    message = reverseEscaping(split[0]);
    permission = split.length > 1 ? split[1] : "";
  }

  @Override
  public String toPacket() {
    return escapingString(message) + ":" + permission;
  }

  @Override
  public String getChannel() {
    return NetworkKey.NETWORK_BROADCAST;
  }

  @Override
  public Object toEvent() {
    return new NetworkBroadcastEvent(message, permission);
  }

}
