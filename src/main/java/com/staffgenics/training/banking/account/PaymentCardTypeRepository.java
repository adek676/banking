package com.staffgenics.training.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentCardTypeRepository extends JpaRepository<PaymentCardType, Long> {

  Optional<PaymentCardType> getAllByCardType(String cardType);
}
