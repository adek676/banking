package com.staffgenics.training.banking.exceptions;

public class AccountBalanceTooLowException extends RuntimeException {

  public AccountBalanceTooLowException(String message) {
    super(message);
  }
}
