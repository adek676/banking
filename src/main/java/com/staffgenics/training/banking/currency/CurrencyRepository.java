package com.staffgenics.training.banking.currency;

import com.staffgenics.training.banking.currency.CurrencyRatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyRatesEntity, Long> {
  Optional<CurrencyRatesEntity> findByCurrencyName(String currency);
}
