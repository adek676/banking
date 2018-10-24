package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.client.ClientEntity;
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

  private String accountNumber;

  private Currency currency;

  private BigDecimal balance;

  static AccountDto createInstance(AccountEntity accountEntity){
    AccountDto accountDto = new AccountDto();
    accountDto.setId(accountEntity.getId());
    accountDto.setClientId(accountEntity.getClientId());
    accountDto.setAccountNumber(accountEntity.getAccountNumber());
    accountDto.setCurrency(accountEntity.getCurrency());
    accountDto.setBalance(accountEntity.getBalance());

    return accountDto;
  }
}
