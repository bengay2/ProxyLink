package com.blockbyblockwest.fest.proxylink.redis;

import java.util.Objects;

public class JedisConfig {

  private final String host;
  private final String password;
  private final int database;
  private final int port;
  private final boolean ssl;

  private final int maxPoolSize;
  private final int maxPoolIdleSize;
  private final int minPoolIdleSize;

  public JedisConfig(String host, String password, int database, int port, boolean ssl,
      int maxPoolSize, int maxPoolIdleSize, int minPoolIdleSize) {
    this.host = host;
    this.password = password;
    this.database = database;
    this.port = port;
    this.ssl = ssl;
    this.maxPoolSize = maxPoolSize;
    this.maxPoolIdleSize = maxPoolIdleSize;
    this.minPoolIdleSize = minPoolIdleSize;
  }

  public String getHost() {
    return host;
  }

  public String getPassword() {
    return password;
  }

  public int getDatabase() {
    return database;
  }

  public int getPort() {
    return port;
  }

  public boolean isSsl() {
    return ssl;
  }

  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  public int getMaxPoolIdleSize() {
    return maxPoolIdleSize;
  }

  public int getMinPoolIdleSize() {
    return minPoolIdleSize;
  }

  @Override
  public String toString() {
    return "Credentials{" +
        "host='" + host + '\'' +
        ", password='" + password + '\'' +
        ", database=" + database +
        ", port=" + port +
        ", ssl=" + ssl +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JedisConfig that = (JedisConfig) o;
    return getDatabase() == that.getDatabase() &&
        getPort() == that.getPort() &&
        isSsl() == that.isSsl() &&
        Objects.equals(getHost(), that.getHost()) &&
        Objects.equals(getPassword(), that.getPassword());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getHost(), getPassword(), getDatabase(), getPort(), isSsl());
  }
}
