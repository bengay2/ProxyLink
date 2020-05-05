package com.blockbyblockwest.fest.proxylink.redis.pubsub.base;

import com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet.PubSubPacket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

public abstract class GenericPacketPubSub extends ReconnectingPubSub {

  private final Logger logger = Logger.getLogger(getClass().getSimpleName());

  private final Map<String, Supplier<? extends PubSubPacket>> channelToPacketMap = new HashMap<>();

  public void registerPacket(String channel, Supplier<? extends PubSubPacket> packetSupplier) {
    channelToPacketMap.put(channel, packetSupplier);
  }

  @Override
  public void onMessage(String channel, String message) {
    try {
      PubSubPacket packet = constructInstanceFromChannel(channel);
      if (packet != null) {
        packet.read(message);
        processPacket(packet);
      } else {
        logger.warning("Failed to read packet " + channel + ": " + message);
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  private PubSubPacket constructInstanceFromChannel(String channel) {
    Supplier<? extends PubSubPacket> supplier = channelToPacketMap.get(channel);
    if (supplier != null) {
      return supplier.get();
    }
    return null;
  }

  @Override
  public String[] getChannels() {
    return channelToPacketMap.keySet().toArray(new String[0]);
  }

  public abstract void processPacket(PubSubPacket packet);

}
