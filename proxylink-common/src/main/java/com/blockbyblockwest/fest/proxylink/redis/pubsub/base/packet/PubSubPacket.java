package com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet;

import java.util.regex.Pattern;
import redis.clients.jedis.Response;
import redis.clients.jedis.commands.MultiKeyCommands;
import redis.clients.jedis.commands.MultiKeyCommandsPipeline;

public abstract class PubSubPacket {

  protected static final Pattern ESCAPING_COLON = Pattern.compile("(?<!\\\\):");

  public PubSubPacket() {
  }

  protected static String escapingString(String str) {
    return str.replaceAll(":", "\\\\:");
  }

  protected static String reverseEscaping(String str) {
    return str.replaceAll("\\\\:", ":");
  }

  public abstract void read(String msg);

  public abstract String toPacket();

  public abstract String getChannel();

  public abstract Object toEvent();

  public Long publish(MultiKeyCommands commands) {
    return commands.publish(getChannel(), toPacket());
  }

  public Response<Long> publish(MultiKeyCommandsPipeline pipe) {
    return pipe.publish(getChannel(), toPacket());
  }

}
