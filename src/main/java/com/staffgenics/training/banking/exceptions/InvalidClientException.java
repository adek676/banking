package com.staffgenics.training.banking.exceptions;

public class InvalidClientException extends RuntimeException {
  public InvalidClientException(String message) {
    super(message);
  }
}
