package com.blockbyblockwest.fest.proxylink.redis.profile;

public class ProfileKey {

  private ProfileKey() {
    throw new AssertionError();
  }

  public static final String NAMESPACE = "profile-service:";

  public static String createKey(String key) {
    return NAMESPACE + key;
  }

  public static final String CACHE_NAMESPACE = createKey("cache:");

  /**
   * Makes a cache key of a specific user. The user may be their UUID or their lowercase name
   *
   * @param user UUID or lowercase name
   * @return redis key
   */
  public static String createCacheKey(String user) {
    return CACHE_NAMESPACE + user;
  }

}
