package com.blockbyblockwest.fest.proxylink.profile;

import com.blockbyblockwest.fest.proxylink.profile.impl.ProfileHolder;
import com.blockbyblockwest.fest.proxylink.profile.impl.ProfileHolder.SkinHolder;
import java.util.Optional;
import java.util.UUID;

public interface Profile {

  static Profile of(UUID uniqueId, String name) {
    return new ProfileHolder(uniqueId, name, null);
  }

  static Profile of(UUID uniqueId, String name, Skin skin) {
    if (skin == null) {
      return of(uniqueId, name);
    }
    return of(uniqueId, name, skin.getValue(), skin.getSignature());
  }

  static Profile of(UUID uniqueId, String name, String value, String signature) {
    return new ProfileHolder(uniqueId, name, new SkinHolder(value, signature));
  }

  UUID getUniqueId();

  String getName();

  Optional<Skin> getSkin();

  interface Skin {

    static Skin of(String value, String signature) {
      return new SkinHolder(value, signature);
    }

    String getValue();

    String getSignature();

  }

}