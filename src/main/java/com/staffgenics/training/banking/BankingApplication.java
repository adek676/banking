package com.staffgenics.training.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Klasa z podstawową konfiguracją aplikacji.
 */
@SpringBootApplication
@EnableConfigurationProperties(BankingProperties.class)
public class BankingApplication {

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication.run(BankingApplication.class, args);
  }
}
