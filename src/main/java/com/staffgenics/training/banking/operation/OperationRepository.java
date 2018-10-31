package com.staffgenics.training.banking.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<OperationEntity, Long> {
  OperationEntity findByIdAndAccountId(Long id, Long accountId);
}
