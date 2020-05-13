package com.blockbyblockwest.fest.proxylink.redis;

import com.blockbyblockwest.fest.proxylink.exception.ServiceException;
import java.util.concurrent.atomic.AtomicBoolean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisBackend {

  private final AtomicBoolean initialized = new AtomicBoolean();

  private JedisPool jedisPool;

  public void initialize(JedisConfig cred) throws ServiceException {

    if (initialized.compareAndSet(false, true)) {
      JedisPoolConfig poolConfig = new JedisPoolConfig();
      poolConfig.setMaxTotal(cred.getMaxPoolSize());
      poolConfig.setMaxIdle(cred.getMaxPoolIdleSize());
      poolConfig.setMinIdle(cred.getMinPoolIdleSize());
      poolConfig.setBlockWhenExhausted(true);

      if (cred.getPassword() == null || cred.getPassword().isEmpty()) {
        jedisPool = new JedisPool(poolConfig, cred.getHost(), cred.getPort(), 10000, null,
            cred.getDatabase(), cred.isSsl());
      } else {
        jedisPool = new JedisPool(poolConfig, cred.getHost(), cred.getPort(), 10000,
            cred.getPassword(), cred.getDatabase(), cred.isSsl());
      }

      // Simple test if a connection can be established
      try (Jedis jedis = jedisPool.getResource()) {
        jedis.ping();
      } catch (JedisConnectionException e) {
        throw new ServiceException(e);
      }

    } else {
      throw new ServiceException("Already initialized.");
    }

  }

  public JedisPool getJedisPool() {
    return jedisPool;
  }

  public void shutdown() {
    if (initialized.get()) {
      try {
        jedisPool.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public boolean isInitialized() {
    return initialized.get();
  }

}
