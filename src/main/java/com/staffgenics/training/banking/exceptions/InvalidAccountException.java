package com.staffgenics.training.banking.exceptions;

public class InvalidAccountException extends RuntimeException {
  public InvalidAccountException(String message) {
    super(message);
  }
}
