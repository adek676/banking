package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.currency.CurrencyRatesEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long clientId;

  @Version
  private Long version;

  private String accountNumber;

  @OneToOne
  private CurrencyRatesEntity currency;

  private BigDecimal balance;

  static AccountEntity createInstance(AccountDto accountDto){
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setClientId(accountDto.getClientId());
    accountEntity.setBalance(accountDto.getBalance());
    accountEntity.setAccountNumber(accountDto.getAccountNumber());

    return accountEntity;
  }
}
