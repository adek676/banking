package com.staffgenics.training.banking.exceptions;

public class EmployeeAlreadyExistsException extends RuntimeException {

  public EmployeeAlreadyExistsException(String message) {
    super(message);
  }
}
