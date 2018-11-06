package com.staffgenics.training.banking.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter(value = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@NoArgsConstructor
public class ClientCriteria {

  private String name;

  private String surname;

  private boolean isVip;
}
