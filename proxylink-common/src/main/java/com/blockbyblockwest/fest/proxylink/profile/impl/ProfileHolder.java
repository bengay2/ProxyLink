package com.blockbyblockwest.fest.proxylink.profile.impl;

import com.blockbyblockwest.fest.proxylink.profile.Profile;
import java.util.Optional;
import java.util.UUID;

public class ProfileHolder implements Profile {

  private final UUID uniqueId;
  private final String name;

  private final SkinHolder skin;

  public ProfileHolder(UUID uniqueId, String name, SkinHolder skin) {
    this.uniqueId = uniqueId;
    this.name = name;
    this.skin = skin;
  }

  @Override
  public UUID getUniqueId() {
    return uniqueId;
  }

  @Override
  public String getName() {
    return name;
  }

  public Optional<Skin> getSkin() {
    return Optional.ofNullable(skin);
  }

  public static class SkinHolder implements Skin {

    private final String value;
    private final String signature;

    public SkinHolder(String value, String signature) {
      this.value = value;
      this.signature = signature;
    }

    @Override
    public String getValue() {
      return value;
    }

    @Override
    public String getSignature() {
      return signature;
    }

  }

}
