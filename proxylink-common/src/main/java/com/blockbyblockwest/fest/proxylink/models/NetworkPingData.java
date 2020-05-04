package com.blockbyblockwest.fest.proxylink.models;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import java.util.List;

public interface NetworkPingData {

  /**
   * Represents the message displayed in the users server list. <p>
   *
   * @return description of the network
   */
  String getDescription() throws ServiceException;

  /**
   * Sets the message displayed to the user in their server list. To use the second line just use a
   * regular line wrap. Formatting is expected with legacy minecraft color codes.
   *
   * @param description of the network
   */
  void setDescription(String description) throws ServiceException;

  /**
   * Represents the message displayed to the user when hovering over the player count.
   *
   * @return the hover message of the network
   */
  List<String> getHoverMessage();

  /**
   * Sets the message displayed to the user when hovering over the player count. Formatting is
   * expected with legacy minecraft color codes.
   *
   * @param hoverMessage shown when hovering over playercount
   */
  void setHoverMessage(List<String> hoverMessage) throws ServiceException;

}
