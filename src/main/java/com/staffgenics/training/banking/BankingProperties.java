package com.staffgenics.training.banking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking")
@Getter
@Setter
public class BankingProperties {

  private int maxCreditCardAmount = 3;
}
