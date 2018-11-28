package com.staffgenics.training.banking.exceptions;

public class CreditCardAmountExceededException extends RuntimeException {
  public CreditCardAmountExceededException(String message) {
    super(message);
  }
}
