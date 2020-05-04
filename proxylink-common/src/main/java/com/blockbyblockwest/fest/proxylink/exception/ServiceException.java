package com.blockbyblockwest.fest.proxylink.exception;

public class ServiceException extends Exception {

  public ServiceException() {
    super();
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String s, Throwable throwable, boolean enableSuppression,
      boolean writableStackTrace) {
    super(s, throwable, enableSuppression, writableStackTrace);
  }

}
