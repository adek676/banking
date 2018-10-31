package com.staffgenics.training.banking.client;

import com.staffgenics.training.banking.common.PeselValidator;

public class ClientValidator {
  private ClientDto clientDto;
  private ClientEntity clientEntity;

  public ClientValidator(ClientDto clientDto, ClientEntity clientEntity) {
    if (!PeselValidator.isPeselValid(clientDto.getPesel())){
      throw new IllegalArgumentException("Pesel is not valid");
    }
    this.clientDto = clientDto;
    this.clientEntity = clientEntity;
  }

  public boolean isPeselValid(){
    return isPeselValid(clientDto.getPesel());
  }

  public boolean isClientCredentialMatching(){
    return isClientCredentialMatching(clientDto, clientEntity);
  }

  public static boolean isPeselValid(String pesel) {
    return PeselValidator.isPeselValid(pesel);
  }

  public static boolean isClientCredentialMatching(ClientDto clientDto, ClientEntity clientEntity){
    return clientDto.getName().equals(clientEntity.getName()) &&
            clientDto.getSurname().equals(clientEntity.getSurname()) &&
            clientDto.getPesel().equals(clientEntity.getPesel());
  }
}
