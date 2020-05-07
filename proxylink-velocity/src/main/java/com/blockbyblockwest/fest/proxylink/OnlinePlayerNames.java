package com.blockbyblockwest.fest.proxylink;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.profile.ProfileService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class OnlinePlayerNames {

  private final NetworkService networkService;
  private final ProfileService profileService;

  private final Cache<UUID, String> localNameCache = CacheBuilder.newBuilder()
      .expireAfterWrite(1, TimeUnit.HOURS)
      .build();

  private List<String> onlinePlayerNames = ImmutableList.of();

  public OnlinePlayerNames(NetworkService networkService, ProfileService profileService) {
    this.networkService = networkService;
    this.profileService = profileService;
  }

  public void update() {
    try {
      Set<UUID> onlineUsers = networkService.getOnlineUsers();
      ImmutableMap<UUID, String> localResult = localNameCache.getAllPresent(onlineUsers);

      if (localResult.size() == onlineUsers.size()) {
        onlinePlayerNames = ImmutableList.copyOf(localResult.values());
      } else {
        List<String> remoteResult = profileService.getProfileNames(
            onlineUsers.stream()
                .filter(uuid -> !localResult.containsKey(uuid))
                .collect(Collectors.toSet())
        );

        onlinePlayerNames = ImmutableList.<String>builder()
            .addAll(remoteResult)
            .addAll(localResult.values())
            .build();
      }
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public List<String> getNames() {
    return onlinePlayerNames;
  }

}
