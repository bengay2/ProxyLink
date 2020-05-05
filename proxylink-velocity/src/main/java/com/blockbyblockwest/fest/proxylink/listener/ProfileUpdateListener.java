package com.blockbyblockwest.fest.proxylink.listener;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.profile.Profile;
import com.blockbyblockwest.fest.proxylink.profile.Profile.Skin;
import com.blockbyblockwest.fest.proxylink.profile.ProfileService;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.GameProfile.Property;
import java.util.UUID;

public class ProfileUpdateListener {

  private final ProfileService profileService;

  public ProfileUpdateListener(ProfileService profileService) {
    this.profileService = profileService;
  }

  @Subscribe(order = PostOrder.FIRST)
  public void onLogin(LoginEvent e) {
    try {
      profileService.update(fromVelocityProfile(e.getPlayer().getGameProfile()));
    } catch (ServiceException ex) {
      ex.printStackTrace();
    }
  }

  public Profile fromVelocityProfile(GameProfile gameProfile) {
    UUID uniqueId = gameProfile.getId();
    String name = gameProfile.getName();

    Property skin = null;

    for (Property property : gameProfile.getProperties()) {
      if (property.getName().equals("textures")) {
        skin = property;
        break;
      }
    }

    return Profile.of(uniqueId, name, skin != null ? Skin.of(skin.getValue(), skin.getSignature())
        : null);
  }

}
