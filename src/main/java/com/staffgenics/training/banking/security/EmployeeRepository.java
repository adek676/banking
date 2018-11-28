package com.staffgenics.training.banking.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String> {

  Optional<EmployeeEntity> findEmployeeByLogin(String login);
}
