package com.staffgenics.training.banking.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class AccountDto {

  private Long id;

  private Long clientId;

  private Long version;

  private String accountNumber;

  private String currency;

  private BigDecimal balance;

  private BigDecimal balanceInPln;

  static AccountDto createInstance(AccountEntity accountEntity){
    AccountDto accountDto = new AccountDto();
    accountDto.setId(accountEntity.getId());
    accountDto.setClientId(accountEntity.getClientId());
    accountDto.setAccountNumber(accountEntity.getAccountNumber());
    accountDto.setCurrency(accountEntity.getCurrency().getCurrencyName());
    accountDto.setBalance(accountEntity.getBalance());
    accountDto.setVersion(accountEntity.getVersion());

    return accountDto;
  }
}
