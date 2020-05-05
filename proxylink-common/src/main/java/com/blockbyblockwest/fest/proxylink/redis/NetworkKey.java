package com.blockbyblockwest.fest.proxylink.redis;

import com.blockbyblockwest.fest.proxylink.models.LinkedProxyServer;
import java.util.UUID;

public class NetworkKey {

  private NetworkKey() {
    throw new AssertionError();
  }

  public static final String NAMESPACE = "proxy-link:";

  public static String createKey(String key) {
    return NAMESPACE + key;
  }

  public static final String PROXY_SET = createKey("proxies");
  public static final String SERVER_SET = createKey("servers");
  public static final String PING_DATA_DESCRIPTION = createKey("ping-data-description");
  public static final String PING_DATA_HOVER = createKey("ping-data-hover");
  public static final String SERVER_REGISTER = createKey("server-register");
  public static final String SERVER_UNREGISTER = createKey("server-unregister");
  public static final String SERVER_UPDATE_PLAYER_COUNT = createKey("server-update-player-count");

  public static final String PROXY_PLAYERS_NAMESPACE = createKey("online-player:");
  public static final String USER_SERVER_NAMESPACE = createKey("user-server:");

  public static final String SERVER_NAMESPACE = createKey("server:");
  public static final String SERVER_TYPE = "type";
  public static final String SERVER_HOST = "host";
  public static final String SERVER_PORT = "port";
  public static final String SERVER_MAX_PLAYER_COUNT = "max-player-count";
  public static final String SERVER_PLAYER_COUNT = "player-count";

  public static final String USER_SWITCH_SERVER_REQUEST = createKey("user-switch-server-request");
  public static final String USER_SWITCH_SERVER = createKey("user-switch-server");
  public static final String USER_DISCONNECT = createKey("user-disconnect");
  public static final String USER_KICK = createKey("user-kick");
  public static final String USER_MESSAGE = createKey("user-message");
  public static final String NETWORK_BROADCAST = createKey("network-broadcast");

  public static String getProxyPlayers(LinkedProxyServer linkedProxyServer) {
    return getProxyPlayers(linkedProxyServer.getId());
  }

  public static String getProxyPlayers(String proxyId) {
    return PROXY_PLAYERS_NAMESPACE + proxyId;
  }

  public static String getServerKey(String serverId) {
    return SERVER_NAMESPACE + serverId;
  }

  public static String getUserServer(UUID userId) {
    return USER_SERVER_NAMESPACE + userId.toString();
  }

}
