package com.blockbyblockwest.fest.proxylink;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.models.BackendServer;
import com.blockbyblockwest.fest.proxylink.models.LinkedProxyServer;
import com.blockbyblockwest.fest.proxylink.models.NetworkPingData;
import com.blockbyblockwest.fest.proxylink.user.MessageType;
import com.blockbyblockwest.fest.proxylink.user.NetworkUser;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface NetworkService {

  /**
   * Connects the network service to the backend.
   */
  void initialize();

  /**
   * Shuts down the connection to the backend.
   */
  void shutdown();

  /**
   * The set of all proxies participating in this network and have not exceeded their heartbeat
   * timeout.
   *
   * @return set of all proxies in the network
   * @throws ServiceException if a connection to the backend fails
   */
  Set<LinkedProxyServer> getProxyServers() throws ServiceException;

  /**
   * The maximum amount of players that can theoretically connect to the server filling all
   * proxies.
   *
   * @return maximum amount of players
   * @throws ServiceException if a connection to the backend fails
   */
  default int getMaxPlayerCount() throws ServiceException {
    return getProxyServers().stream().mapToInt(LinkedProxyServer::getMaxPlayerCount).sum();
  }

  /**
   * Represents the data served when the network is pinged with a minecraft client.
   *
   * @return data to be forwarded to a client
   * @throws ServiceException if a connection to the backend fails
   */
  NetworkPingData getPingData() throws ServiceException;

  /**
   * The set of all users connected to the network.<p> Represented in their unique identifiers.
   *
   * @return set of all users online.
   * @throws ServiceException if a connection to the backend fails
   */
  Set<UUID> getOnlineUsers() throws ServiceException;

  /**
   * The player count connected to the network.
   *
   * @return amount of players online
   * @throws ServiceException if a connection to the backend fails
   */
  default int getOnlineUserCount() throws ServiceException {
    return getOnlineUsers().size();
  }

  /**
   * Checks wether or not there is a user connected to the network with the provided unique
   * identifier.
   *
   * @param uniqueId of the user
   * @return {@code true} if the user is online
   * @throws ServiceException if a connection to the backend fails
   */
  default boolean isUserOnline(UUID uniqueId) throws ServiceException {
    return getOnlineUsers().contains(uniqueId);
  }

  /**
   * Retrieves a representation of all backend servers registered to this network.
   *
   * @return set of servers registered
   * @throws ServiceException if a connection to the backend fails
   */
  Collection<? extends BackendServer> getServers() throws ServiceException;

  Optional<BackendServer> getServerData(String id) throws ServiceException;

  /**
   * This method is called periodically by all proxies participating in this network.
   *
   * @param id identifier of the proxy
   * @throws ServiceException if a connection to the backend fails
   */
  void proxyHeartBeat(String id) throws ServiceException;

  /**
   * Called by a proxy when they disconnect from the network.
   *
   * @param id of the proxy
   * @throws ServiceException if a connection to the backend fails
   */
  void removeProxy(String id) throws ServiceException;

  /**
   * This method is called periodically by all servers participating in this network.
   *
   * @param id identifier of the server
   * @throws ServiceException if a connection to the backend fails
   */
  void serverHeartBeat(String id) throws ServiceException;

  /**
   * Called by a backend when they disconnect from the network.
   *
   * @param id of  server
   * @throws ServiceException if a connection to the backend fails
   */
  void removeServer(String id) throws ServiceException;

  /**
   * Registers a server to this network.
   *
   * @param id the identifier of the server
   * @param serverType type of the server
   * @param host ip address of the server
   * @param port port of the server
   * @throws ServiceException on failure
   */
  BackendServer registerServer(String id, ServerType serverType, String host, int port,
      int maxPlayerCount) throws ServiceException;

  // User related methods

  /**
   * Just creates a helper class making user functions more convienient to use.
   */
  default NetworkUser getUser(UUID uniqueId) {
    return new NetworkUser(uniqueId, this);
  }

  /**
   * Backend server.
   */
  Optional<String> getServerIdOfUser(UUID uniqueId) throws ServiceException;

  /**
   * Called by the proxy when a player connects.
   */
  void connectUserToProxy(UUID uniqueId, String proxyId) throws ServiceException;

  /**
   * Called by the proxy when a player disconnects
   */
  void disconnectUser(UUID uniqueId, String proxyId) throws ServiceException;

  /**
   * Called by the proxy on server switch
   */
  void switchServer(UUID uniqueId, String toServerId) throws ServiceException;

  /**
   * {@link NetworkService#sendUserToServer(UUID, String)}
   */
  default void sendUserToServer(UUID uniqueId, BackendServer toServer) throws ServiceException {
    sendUserToServer(uniqueId, toServer.getId());
  }

  /**
   * Fire and forget server connection. No guarantee for success (server might be full etc.).
   */
  void sendUserToServer(UUID uniqueId, String toServer) throws ServiceException;

  void kickUser(UUID uniqueId, String reason) throws ServiceException;

  void sendMessageToUser(UUID uniqueId, String message, MessageType type) throws ServiceException;

  default void broadcast(String message) throws ServiceException {
    broadcast(message, "");
  }

  void broadcast(String message, String permission) throws ServiceException;

}
