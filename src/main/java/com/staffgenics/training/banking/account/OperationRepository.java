package com.staffgenics.training.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<OperationEntity, Long> {
  OperationEntity findByIdAndAccountId(Long id, Long accountId);

  @Query(
      "SELECT operation FROM OperationEntity operation " +
      "WHERE operation.accountId = :id " +
      "AND operation.amount >= :minAmount " +
      "AND operation.amount <= :maxAmount " +
      "AND operation.operationDate BETWEEN :dateFrom AND :dateEnd")
  List<OperationEntity> findOperationEntitiesByParams(@Param("id") Long id, @Param("dateFrom")Date dateFrom, @Param("dateEnd")Date dateEnd,
                                                      @Param("minAmount")BigDecimal minAmount, @Param("maxAmount")BigDecimal maxAmount);

}