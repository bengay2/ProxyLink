package com.blockbyblockwest.fest.proxylink.redis.pubsub.packet;

import com.blockbyblockwest.fest.proxylink.event.backendserver.BackendServerUpdatePlayerCountEvent;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;

public class BackendServerUpdatePlayerCountPacket extends PubSubPacket {

  private String serverId;
  private int playerCount;

  public BackendServerUpdatePlayerCountPacket() {
  }

  public BackendServerUpdatePlayerCountPacket(String serverId, int playerCount) {
    this.serverId = serverId;
    this.playerCount = playerCount;
  }

  @Override
  public void read(String msg) {
    String[] split = ESCAPING_COLON.split(msg);
    serverId = split[0];
    playerCount = Integer.parseInt(split[1]);
  }

  @Override
  public String toPacket() {
    return serverId + ":" + playerCount;
  }

  @Override
  public String getChannel() {
    return NetworkKey.SERVER_UPDATE_PLAYER_COUNT;
  }

  @Override
  public Object toEvent() {
    return new BackendServerUpdatePlayerCountEvent(serverId, playerCount);
  }

  public String getServerId() {
    return serverId;
  }

  public int getPlayerCount() {
    return playerCount;
  }

}
