package com.staffgenics.training.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Klasa z podstawową konfiguracją aplikacji.
 */
@SpringBootApplication
@EnableConfigurationProperties(BankingProperties.class)
public class BankingApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingApplication.class, args);
  }
}
