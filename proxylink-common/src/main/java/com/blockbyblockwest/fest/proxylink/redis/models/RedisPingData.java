package com.blockbyblockwest.fest.proxylink.redis.models;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.models.NetworkPingData;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import java.util.Collections;
import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

public class RedisPingData implements NetworkPingData {

  private final JedisPool jedisPool;

  private final String description;
  private final List<String> hoverMessage;

  public RedisPingData(JedisPool jedisPool, String description, List<String> hoverMessage) {
    this.jedisPool = jedisPool;
    this.description = description != null ? description : "";
    this.hoverMessage = hoverMessage != null ? hoverMessage : Collections.emptyList();
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) throws ServiceException {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.set(NetworkKey.PING_DATA_DESCRIPTION, description);
    } catch (JedisException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public List<String> getHoverMessage() {
    return hoverMessage;
  }

  @Override
  public void setHoverMessage(List<String> hoverMessage) throws ServiceException {
    Collections.reverse(hoverMessage);
    try (Jedis jedis = jedisPool.getResource()) {
      Pipeline pipe = jedis.pipelined();
      pipe.del(NetworkKey.PING_DATA_HOVER);
      pipe.lpush(NetworkKey.PING_DATA_HOVER, hoverMessage.toArray(new String[0]));
      pipe.sync();
    } catch (JedisException e) {
      throw new ServiceException(e);
    }
  }

}