package com.staffgenics.training.banking.client;

import com.staffgenics.training.banking.account.AccountRepository;
import com.staffgenics.training.banking.account.AccountService;
import com.staffgenics.training.banking.exceptions.InvalidClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Serwis obsługujący klientów.
 */
@Service
@Slf4j
class ClientService {

  private final ClientRepository clientRepository;

  private final AccountService accountService;

  ClientService(ClientRepository clientRepository, AccountService accountService) {
    this.clientRepository = clientRepository;
    this.accountService = accountService;
  }

  List<ClientDto> getClients() {
    log.info("Pobieramy wszystkich klientów");
    return clientRepository.findAll().stream()
        .map(ClientDto::createInstance)
        .collect(Collectors.toList());
  }

  ClientDto getClient(Long id) {
    log.info("Pobieramy klienta o id: {}", id);
    ClientEntity clientEntity = findClient(id);
    return ClientDto.createInstance(clientEntity);
  }

  ClientDto getClientByPesel(String pesel) {
    Optional<ClientEntity> clientEntityOptional = findClientByPesel(pesel);
    if (!clientEntityOptional.isPresent()){
      throw new IllegalArgumentException("Klient o podanym numerze pesel nie istnieje");
    }
    return ClientDto.createInstance(clientEntityOptional.get());
  }

  ClientDto getClientByParams(String name, String surname, boolean isVip) {
    return ClientDto.createInstance(findClient(name, surname, isVip));
  }

  Long createClient(ClientDto clientDto) {
    log.info("Dodajemy nowego klienta");

    if (clientDto.isResident()){
      validateClient(clientDto);
    }else {
      validateForeigner(clientDto);
      addCustomPesel(clientDto);
    }

    ClientEntity clientEntity = ClientEntity.createInstance(clientDto);
    clientRepository.save(clientEntity);
    return clientEntity.getId();
  }

  void editClient(ClientDto clientDto, Long id) {
    log.info("Edytujemy klienta o id: {}", id);
    ClientEntity clientEntity = findClient(id);
    if (ClientValidator.isClientCredentialMatching(clientDto, clientEntity)) {
      throw new InvalidClientException("Dane klienta sie nie zgadzaja");
    }
    clientEntity.update(clientDto);
    clientRepository.save(clientEntity);
  }

  @Transactional
  public boolean deleteClient(Long clientId){
    log.info("Dezaktywacja klienta o id: " + clientId);
    ClientEntity clientEntity = findClient(clientId);
    clientEntity.getAccounts().forEach( account -> accountService.deleteAccount(account.getId()));
    clientEntity.deactivate();
    clientRepository.save(clientEntity);
    return true;
  }

  private void validateClient(ClientDto clientDto){
    if (!ClientValidator.isPeselValid(clientDto.getPesel())) {
      throw new IllegalArgumentException("Numer pesel nie jest prawidłowy");
    }

    findClientByPesel(clientDto.getPesel()).ifPresent(clientEntity -> compareClientPersonalData(clientDto, clientEntity));
  }

  private void validateForeigner(ClientDto clientDto){
    Optional<ClientEntity> clientEntityOptional = findClientOptional(clientDto.getName(), clientDto.getSurname(), clientDto.isVip());
    if (clientEntityOptional.isPresent()){
      throw new RuntimeException("Klient o tych danych juz istnieje");
    }
  }

  private void compareClientPersonalData(ClientDto clientDto, ClientEntity clientEntity) {
    if (ClientValidator.isClientCredentialMatching(clientDto, clientEntity)) {
      throw new InvalidClientException("Podany Klient juz istnieje");
    } else {
      throw new InvalidClientException("Klient o podanym peselu juz istnieje");
    }
  }

  private ClientEntity findClient(Long id) {
    Optional<ClientEntity> clientEntityOptional = clientRepository.findById(id);
    if (!clientEntityOptional.isPresent()) {
      throw new IllegalArgumentException("Brak klienta w bazie danych");
    }
    return clientEntityOptional.get();
  }

  private Optional<ClientEntity> findClientOptional(String name, String surname, boolean isVip){
    return clientRepository.findClientByParams(name, surname, isVip);
  }

  private ClientEntity findClient(String name, String surname, boolean isVip) {
    Optional<ClientEntity> clientEntityOptional = findClientOptional(name, surname, isVip);
    if (!clientEntityOptional.isPresent()) {
      throw new IllegalArgumentException("No client with such params");
    }
    return clientEntityOptional.get();
  }

  private Optional<ClientEntity> findClientByPesel(String pesel) {
    return clientRepository.findClientByPesel(pesel);
  }

  private void addCustomPesel(ClientDto clientDto){
    Random random = new Random();
    int randomNumber = random.nextInt(100000000);
    if (findClientByPesel(String.valueOf(randomNumber)).isPresent()){
      throw new IllegalArgumentException("Klient o podanym, tymczasowym numerze pesel juz istnieje");
    }
    clientDto.setPesel(String.valueOf(randomNumber));
  }
}
