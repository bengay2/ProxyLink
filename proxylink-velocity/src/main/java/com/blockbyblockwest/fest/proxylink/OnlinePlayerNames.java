package com.blockbyblockwest.fest.proxylink;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.profile.ProfileService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OnlinePlayerNames {

  private final NetworkService networkService;
  private final ProfileService profileService;

  private List<String> onlinePlayerNames = new ArrayList<>();

  public OnlinePlayerNames(NetworkService networkService, ProfileService profileService) {
    this.networkService = networkService;
    this.profileService = profileService;
  }

  public void update() {
    try {
      Set<UUID> onlineUsers = networkService.getOnlineUsers();
      onlinePlayerNames = profileService.getProfileNames(onlineUsers);
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public List<String> getNames() {
    return onlinePlayerNames;
  }

}
