package com.neolore.card.config;


import org.springframework.dao.DataAccessException;

public class ShouldRetryTransactionException extends DataAccessException {

  public ShouldRetryTransactionException(String msg) {
    super(msg);
  }

  public ShouldRetryTransactionException(Throwable cause) {
    super("Transaction should be re-tried.", cause);
  }

  public ShouldRetryTransactionException(String msg, Throwable cause) {
    super(msg, cause);
  }
}

