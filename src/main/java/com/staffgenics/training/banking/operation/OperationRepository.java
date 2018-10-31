package com.staffgenics.training.banking.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<OperationEntity, Long> {
  OperationEntity findByIdAndAccountId(Long id, Long accountId);

  @Query("SELECT operation FROM OperationEntity operation " +
      "WHERE operation.accountId = :id AND operation.amount >= :minAmount AND operation.amount <= :maxAmount AND operation.operationDate BETWEEN :dateFrom AND :dateEnd")
  Optional<OperationEntity> findOperationEntitiesByParams(Long id, Date dateFrom, Date dateEnd, BigDecimal minAmount, BigDecimal maxAmount);

}
