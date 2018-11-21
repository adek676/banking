package com.staffgenics.training.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Long> {
  Optional<OperationTypeEntity> findOperationTypeEntityByName(String name);
}
