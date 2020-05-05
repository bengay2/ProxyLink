package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.backendserver.BackendServerUnregisterEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;

public class BackendServerUnregisterPacket extends PubSubPacket {

  private String id;

  public BackendServerUnregisterPacket() {
  }

  public BackendServerUnregisterPacket(String id) {
    this.id = id;
  }

  @Override
  public void read(String msg) {
    id = msg;
  }

  @Override
  public String toPacket() {
    return id;
  }

  @Override
  public String getChannel() {
    return NetworkKey.SERVER_UNREGISTER;
  }

  @Override
  public Object toEvent() {
    return new BackendServerUnregisterEvent(id);
  }

  public String getId() {
    return this.id;
  }

}
