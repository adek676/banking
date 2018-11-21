package com.staffgenics.training.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

//  @Query("select payment from PaymentCard payment where payment.account.id = :id")
  List<PaymentCard> findAllByAccountId(Long id);

  Optional<PaymentCard> findByNumber(String number);
}
