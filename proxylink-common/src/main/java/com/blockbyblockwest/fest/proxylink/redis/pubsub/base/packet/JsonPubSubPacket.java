package com.blockbyblockwest.fest.proxylink.redis.pubsub.base.packet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public abstract class JsonPubSubPacket extends PubSubPacket {

  private static final Gson GSON = new Gson();

  @Override
  public void read(String msg) {
    populate(msg, getClass(), this);
  }

  @Override
  public String toPacket() {
    return GSON.toJson(this);
  }


  private static <T> void populate(String json, Class<? extends T> type, T into) {
    populate(new GsonBuilder(), json, type, into);
  }

  private static <T> void populate(GsonBuilder gsonBuilder, String json, Class<? extends T> type,
      T into) {
    gsonBuilder.registerTypeAdapter(type, (InstanceCreator<T>) t -> into).create()
        .fromJson(json, type);
  }

}
