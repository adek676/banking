package com.staffgenics.training.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * Klasa z podstawową konfiguracją aplikacji.
 */
@SpringBootApplication
@EnableConfigurationProperties(BankingProperties.class)
@EnableScheduling
public class BankingApplication {

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(BankingApplication.class, args);
  }
}
