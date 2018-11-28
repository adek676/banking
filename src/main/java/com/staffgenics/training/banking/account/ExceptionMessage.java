package com.staffgenics.training.banking.account;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExceptionMessage {

  private String message;

  private LocalDate date;

  public ExceptionMessage(String message) {
    this.message = message;
    this.date = LocalDate.now();
  }
}
