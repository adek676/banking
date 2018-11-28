package com.staffgenics.training.banking.exceptions;

public class CurrencyNotFoundException extends RuntimeException {

  public CurrencyNotFoundException(String message) {
    super(message);
  }
}
