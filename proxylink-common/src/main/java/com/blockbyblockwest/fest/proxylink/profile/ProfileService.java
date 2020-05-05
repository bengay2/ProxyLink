package com.blockbyblockwest.fest.proxylink.profile;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface ProfileService {

  Profile CONSOLE_PROFILE = Profile.of(new UUID(0, 0), "CONSOLE");

  /**
   * Retrieves a users profile by their unique identifier.
   *
   * @param uniqueId of the user
   * @return profile of the user
   * @throws ServiceException on failure to retrieve
   */
  Optional<Profile> getProfile(UUID uniqueId) throws ServiceException;

  /**
   * Retrieves user profiles by their unique identifier.
   *
   * @param uuidList of the user
   * @return profile of the user
   * @throws ServiceException on failure to retrieve
   */
  Map<UUID, Optional<Profile>> getProfiles(Collection<UUID> uuidList) throws ServiceException;

  default List<String> getProfileNames(Collection<UUID> uuidList) throws ServiceException {
    return getProfiles(uuidList).values().stream()
        .filter(Optional::isPresent).map(Optional::get).map(Profile::getName)
        .sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
  }

  /**
   * Retrieves a users profile by their name.
   *
   * @param name of the user
   * @return profile of the user
   * @throws ServiceException on failure to retrieve
   */
  Optional<Profile> getProfile(String name) throws ServiceException;

  /**
   * Retrieves user profiles by their name.
   *
   * @param nameList of the user
   * @return profile of the user
   * @throws ServiceException on failure to retrieve
   */
  List<Optional<Profile>> getProfilesByName(Collection<String> nameList) throws ServiceException;

  /**
   * Retrieves a users unique identifier by their name.
   *
   * @param name of the user
   * @return uuid of the user
   * @throws ServiceException on failure to retrieve
   */
  Optional<UUID> getUniqueIdByName(String name) throws ServiceException;

  /**
   * Updates the underlying service with fresh profile information of our user.
   *
   * @param profile of the user
   * @throws ServiceException on failure to update
   */
  void update(Profile profile) throws ServiceException;

}
