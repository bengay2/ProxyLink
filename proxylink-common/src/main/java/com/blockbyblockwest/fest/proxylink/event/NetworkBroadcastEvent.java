package com.blockbyblockwest.fest.proxylink.event;

public class NetworkBroadcastEvent {

  private final String message;
  private final String permission;

  public NetworkBroadcastEvent(String message, String permission) {
    this.message = message;
    this.permission = permission;
  }

  public String getMessage() {
    return message;
  }

  public String getPermission() {
    return permission;
  }

}
