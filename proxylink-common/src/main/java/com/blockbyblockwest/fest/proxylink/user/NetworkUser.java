package com.blockbyblockwest.fest.proxylink.user;

import com.blockbyblockwest.fest.proxylink.NetworkService;
import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.models.BackendServer;
import java.util.Optional;
import java.util.UUID;

public class NetworkUser {

  private final UUID uniqueId;
  private final NetworkService networkService;

  public NetworkUser(UUID uniqueId,
      NetworkService networkService) {
    this.uniqueId = uniqueId;
    this.networkService = networkService;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public NetworkService getNetworkService() {
    return networkService;
  }

  public void sendMessage(String message) throws ServiceException {
    networkService.sendMessageToUser(uniqueId, message, MessageType.STRING);
  }

  public void sendComponent(String jsonComponent) throws ServiceException {
    networkService
        .sendMessageToUser(uniqueId, jsonComponent, MessageType.COMPONENT);
  }

  public void kick(String reason) throws ServiceException {
    networkService.kickUser(uniqueId, reason);
  }

  public boolean isOnline() throws ServiceException {
    return networkService.isUserOnline(uniqueId);
  }

  public void sendToServer(String serverId) throws ServiceException {
    networkService.sendUserToServer(uniqueId, serverId);
  }

  public void sendToServer(BackendServer backendServer) throws ServiceException {
    networkService.sendUserToServer(uniqueId, backendServer);
  }

  public Optional<BackendServer> getServer() throws ServiceException {
    Optional<String> server = networkService.getServerIdOfUser(uniqueId);
    if (server.isPresent()) {
      networkService.getServerData(server.get());
    }
    return Optional.empty();
  }

}