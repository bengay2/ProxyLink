package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.ServerType;
import com.blockbyblockwest.fest.proxylink.event.backendserver.BackendServerRegisterEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.models.RedisBackendServer;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;

public class BackendServerRegisterPacket extends PubSubPacket {

  private RedisBackendServer serverData;

  public BackendServerRegisterPacket() {
  }

  public BackendServerRegisterPacket(RedisBackendServer serverData) {
    this.serverData = serverData;
  }

  @Override
  public void read(String msg) {
    String[] split = ESCAPING_COLON.split(msg);
    serverData = new RedisBackendServer(split[0], ServerType.valueOf(split[1]),
        split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]));
  }

  @Override
  public String toPacket() {
    return serverData.getId() + ":"
        + serverData.getServerType().name() + ":"
        + serverData.getHost() + ":"
        + serverData.getPort() + ":"
        + serverData.getMaxPlayerCount();
  }

  @Override
  public String getChannel() {
    return NetworkKey.SERVER_REGISTER;
  }

  @Override
  public Object toEvent() {
    return new BackendServerRegisterEvent(serverData);
  }

  public RedisBackendServer getServerData() {
    return this.serverData;
  }

}
