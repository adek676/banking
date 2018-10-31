package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.common.NrbNumberValidator;

public class AccountValidator {
  public static boolean isAccountDtoValid(AccountDto accountDto){
    return NrbNumberValidator.isNrbNumberValid(accountDto.getAccountNumber());
  }
}
