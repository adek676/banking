package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.currency.CurrencyRatesEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Where(clause = "inactive='false'")
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long clientId;

  @Version
  private Long version;

  private String accountNumber;

  @ManyToOne
  private CurrencyRatesEntity currency;

  @OneToMany(mappedBy = "account")
  private List<PaymentCard> cards;

  private BigDecimal balance;

  private boolean inactive;

  static AccountEntity createInstance(AccountDto accountDto, String accountNumber, CurrencyRatesEntity currency){
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setClientId(accountDto.getClientId());
    accountEntity.setBalance(accountDto.getBalance());
    accountEntity.setAccountNumber(accountNumber);
    accountEntity.setCurrency(currency);
    accountEntity.setInactive(false);
    return accountEntity;
  }

  static void deactivate(AccountEntity accountEntity){
    accountEntity.setInactive(true);
  }
}
