package com.staffgenics.training.banking.operation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Getter
@Setter
@NoArgsConstructor
public class OperationFilterDto {
  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
  private String dateFrom;
  private String dateEnd;
  private BigDecimal minAmount;
  private BigDecimal maxAmount;
  private String pesel;
}
