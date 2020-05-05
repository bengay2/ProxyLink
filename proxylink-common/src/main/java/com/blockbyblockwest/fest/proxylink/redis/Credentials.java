package com.blockbyblockwest.fest.proxylink.redis;

import java.util.Objects;

public class Credentials {

  private final String host;
  private final String password;
  private final int database;
  private final int port;

  public Credentials(String host, String password, int database, int port) {
    this.host = host;
    this.password = password;
    this.database = database;
    this.port = port;
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

  @Override
  public String toString() {
    return "Credentials{" +
        "host='" + host + '\'' +
        ", password='" + password + '\'' +
        ", database=" + database +
        ", port=" + port +
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
    Credentials that = (Credentials) o;
    return getDatabase() == that.getDatabase() &&
        getPort() == that.getPort() &&
        Objects.equals(getHost(), that.getHost()) &&
        Objects.equals(getPassword(), that.getPassword());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getHost(), getPassword(), getDatabase(), getPort());
  }

}
