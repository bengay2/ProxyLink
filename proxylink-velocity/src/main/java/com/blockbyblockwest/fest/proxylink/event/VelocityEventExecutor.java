package com.blockbyblockwest.fest.proxylink.event;

import com.blockbyblockwest.fest.proxylink.EventExecutor;
import com.velocitypowered.api.event.EventManager;

public class VelocityEventExecutor implements EventExecutor {

  private final EventManager eventManager;

  public VelocityEventExecutor(EventManager eventManager) {
    this.eventManager = eventManager;
  }

  @Override
  public void postEvent(Object event) {
    eventManager.fireAndForget(event);
  }

}
