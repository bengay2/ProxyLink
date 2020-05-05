package com.blockbyblockwest.fest.proxylink.redis.models;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import com.blockbyblockwest.fest.proxylink.models.LinkedProxyServer;
import com.blockbyblockwest.fest.proxylink.redis.NetworkKey;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

public class RedisLinkedProxyServer extends RedisServer implements LinkedProxyServer {

  private final JedisPool jedisPool;

  public RedisLinkedProxyServer(String id, JedisPool jedisPool) {
    super(id);
    this.jedisPool = jedisPool;
  }

  @Override
  public Set<UUID> getOnlineUsers() throws ServiceException {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.smembers(NetworkKey.getProxyPlayers(getId()))
          .stream()
          .map(UUID::fromString)
          .collect(Collectors.toSet());
    } catch (JedisException ex) {
      throw new ServiceException(ex);
    }
  }

}
