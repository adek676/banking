package com.staffgenics.training.banking.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium klientów.
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

  @Query("select client from ClientEntity client where client.pesel = :pesel")
  Optional<ClientEntity> findClientByPesel(String pesel);

  @Query("select client from ClientEntity client where client.name = :name and client.surname = :surname and client.vip = :isVip")
  Optional<ClientEntity> findClientByParams(String name, String surname, boolean isVip);
}
