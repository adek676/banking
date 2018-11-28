package com.staffgenics.training.banking.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDto {

  private String login;

  private String password;

  private String name;
}
