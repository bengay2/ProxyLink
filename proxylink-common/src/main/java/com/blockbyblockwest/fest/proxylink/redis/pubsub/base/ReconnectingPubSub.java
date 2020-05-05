package com.blockbyblockwest.fest.proxylink.redis.pubsub.base;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public abstract class ReconnectingPubSub extends JedisPubSub {

  private final Logger logger = Logger.getLogger(getClass().getSimpleName());

  private final AtomicBoolean shutdown = new AtomicBoolean();

  private Thread listeningThread;
  private int retryCount;

  public void start(JedisPool pool) {
    listeningThread = new Thread(() -> {
      try (Jedis jedis = pool.getResource()) {
        logger.info("Connecting...");
        jedis.subscribe(this, getChannels());
      } catch (Throwable ex) {
        if (retryCount++ == 0) {
          ex.printStackTrace();
          logger.severe("Reconnecting in 5 seconds...");
        } else {
          logger.severe("Reconnecting in 5 seconds. Cause: " + ex.getMessage());
        }
        onConnectionLost();

        if (!shutdown.get()) {
          try {
            Thread.sleep(5 * 1000);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
          start(pool);
        }
      }
    });
    listeningThread.start();
  }

  public void teardown() {
    shutdown.set(true);
    if (listeningThread != null && isSubscribed()) {
      unsubscribe();
      try {
        listeningThread.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }


  public void onConnectionLost() {
  }

  public abstract String[] getChannels();

}
