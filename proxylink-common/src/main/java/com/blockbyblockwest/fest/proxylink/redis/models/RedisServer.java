package com.blockbyblockwest.fest.proxylink.redis.models;

import com.blockbyblockwest.fest.proxylink.models.Server;

public class RedisServer implements Server {

  private final String id;

  public RedisServer(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "RedisServer{" +
        "id='" + id + '\'' +
        '}';
  }

}
