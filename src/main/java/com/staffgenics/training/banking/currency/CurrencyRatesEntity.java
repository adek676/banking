package com.staffgenics.training.banking.currency;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "currency_rates")
@Getter
@Setter
public class CurrencyRatesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "currency")
  private String currencyName;

  @Column(name = "rate")
  private BigDecimal conversionRate;

}
