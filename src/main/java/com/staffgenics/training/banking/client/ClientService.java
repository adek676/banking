package com.staffgenics.training.banking.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.staffgenics.training.banking.exceptions.InvalidClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serwis obsługujący klientów.
 */
@Service
@Slf4j
class ClientService {

  private final ClientRepository clientRepository;

  @Autowired
  ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
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

  Long createClient(ClientDto clientDto) {
    log.info("Dodajemy nowego klienta");
    if (!ClientValidator.isPeselValid(clientDto.getPesel())){
      throw new IllegalArgumentException("Numer pesel nie jest prawidłowy");
    }

    findClientByPesel(clientDto.getPesel()).ifPresent(clientEntity -> compareClientPersonalData(clientDto,clientEntity));

    ClientEntity clientEntity = ClientEntity.createInstance(clientDto);
    clientRepository.save(clientEntity);
    return clientEntity.getId();
  }

  void editClient(ClientDto clientDto, Long id) {
    log.info("Edytujemy klienta o id: {}", id);
    ClientEntity clientEntity = findClient(id);
    if (ClientValidator.isClientCredentialMatching(clientDto, clientEntity)){
      throw new InvalidClientException("Dane klienta sie nie zgadzaja");
    }
    clientEntity.update(clientDto);
    clientRepository.save(clientEntity);
  }

  private void compareClientPersonalData(ClientDto clientDto, ClientEntity clientEntity) {
    if (ClientValidator.isClientCredentialMatching(clientDto, clientEntity)){
      throw new InvalidClientException("Podany Klient juz istnieje");
    }else {
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

  private Optional<ClientEntity> findClientByPesel(String pesel){
    return clientRepository.findClientByPesel(pesel);
  }
}
