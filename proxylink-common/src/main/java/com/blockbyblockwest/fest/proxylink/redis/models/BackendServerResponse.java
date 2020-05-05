package com.blockbyblockwest.fest.proxylink.redis.models;

import com.blockbyblockwest.fest.proxylink.ServerType;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import java.util.Map;
import redis.clients.jedis.Response;

public class BackendServerResponse {

  private final String id;
  private final Response<Map<String, String>> responseMap;

  public BackendServerResponse(String id, Response<Map<String, String>> responseMap) {
    this.id = id;
    this.responseMap = responseMap;
  }

  public RedisBackendServer toServer() {
    Map<String, String> serverInfo = responseMap.get();

    ServerType serverType = ServerType.valueOf(serverInfo.get(NetworkKey.SERVER_TYPE));
    RedisBackendServer server = new RedisBackendServer(id, serverType,
        serverInfo.get(NetworkKey.SERVER_HOST),
        Integer.parseInt(serverInfo.get(NetworkKey.SERVER_PORT)),
        Integer.parseInt(serverInfo.get(NetworkKey.SERVER_MAX_PLAYER_COUNT)));
    server.setPlayerCount(Integer.parseInt(serverInfo.get(NetworkKey.SERVER_PLAYER_COUNT)));

    return server;
  }

}
