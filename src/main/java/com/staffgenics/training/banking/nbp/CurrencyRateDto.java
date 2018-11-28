package com.staffgenics.training.banking.nbp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyRateDto {

  String currency;

  String code;

  BigDecimal mid;
}
