package com.blockbyblockwest.fest.proxylink.redis.pubsub;

import com.blockbyblockwest.fest.proxylink.EventExecutor;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.GenericPacketPubSub;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.BackendServerRegisterPacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.BackendServerUnregisterPacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.NetworkBroadcastPacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.UserDisconnectPacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.UserKickPacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.UserMessagePacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.UserSwitchServerPacket;
import com.blockbyblockwest.fest.proxylink.redis.pubsub.packet.UserSwitchServerRequestPacket;

public class NetworkPubSub extends GenericPacketPubSub {

  private final EventExecutor eventExecutor;
  private final LocalNetworkState localNetworkState;

  public NetworkPubSub(EventExecutor eventExecutor, LocalNetworkState localNetworkState) {
    this.eventExecutor = eventExecutor;
    this.localNetworkState = localNetworkState;

    registerPacket(NetworkKey.SERVER_REGISTER, BackendServerRegisterPacket::new);
    registerPacket(NetworkKey.SERVER_UNREGISTER, BackendServerUnregisterPacket::new);

    registerPacket(NetworkKey.NETWORK_BROADCAST, NetworkBroadcastPacket::new);

    registerPacket(NetworkKey.USER_DISCONNECT, UserDisconnectPacket::new);
    registerPacket(NetworkKey.USER_KICK, UserKickPacket::new);
    registerPacket(NetworkKey.USER_MESSAGE, UserMessagePacket::new);
    registerPacket(NetworkKey.USER_SWITCH_SERVER, UserSwitchServerPacket::new);
    registerPacket(NetworkKey.USER_SWITCH_SERVER_REQUEST, UserSwitchServerRequestPacket::new);

  }

  @Override
  public void processPacket(PubSubPacket packet) {
    updateLocalNetworkState(packet);
    eventExecutor.postEvent(packet);
  }

  private void updateLocalNetworkState(PubSubPacket packet) {
    if (packet instanceof BackendServerRegisterPacket) {

      BackendServerRegisterPacket register = (BackendServerRegisterPacket) packet;
      localNetworkState.getServerInfo()
          .put(register.getServerData().getId(), register.getServerData());

    } else if (packet instanceof BackendServerUnregisterPacket) {

      localNetworkState.getServerInfo().remove(((BackendServerUnregisterPacket) packet).getId());

    } else if (packet instanceof UserSwitchServerPacket) {

      UserSwitchServerPacket switchPacket = (UserSwitchServerPacket) packet;
      localNetworkState.getUserServerMap()
          .put(switchPacket.getUniqueId(), switchPacket.getToServer());

    } else if (packet instanceof UserDisconnectPacket) {

      localNetworkState.getUserServerMap().remove(((UserDisconnectPacket) packet).getUniqueId());

    }
  }

  @Override
  public void onConnectionLost() {
    super.onConnectionLost();
  }
}
