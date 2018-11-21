package com.staffgenics.training.banking.client;

import com.staffgenics.training.banking.account.AccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class ClientServiceIT {

  @MockBean
  ClientRepository clientRepository;

  ClientService clientService;

  @MockBean
  AccountService accountService;

  @Before
  public void setUp() {
    clientService = new ClientService(clientRepository, accountService);
  }

  @Test
  public void testGetClient() {
    //given
    ClientEntity clientEntity = new ClientEntity(2L, "malysz", "adam", "12345699125", "daniel", false, true);

    //when
    Mockito.when(clientRepository.findById(clientEntity.getId())).thenReturn(Optional.of(clientEntity));
    ClientDto foundClient = clientService.getClient(clientEntity.getId());

    //then
    Assert.assertEquals(clientEntity.getId(), foundClient.getId());
    Assert.assertEquals(clientEntity.getName(), foundClient.getName());
    Assert.assertEquals(clientEntity.getPesel(), foundClient.getPesel());
    Assert.assertEquals(clientEntity.getSurname(), foundClient.getSurname());
    Assert.assertEquals(clientEntity.getSecondName(), foundClient.getSecondName());
  }

  @Test
  public void testGetAllClients() {
    //given
    List<ClientEntity> allClients = Arrays.asList(
        new ClientEntity(1L, "mateja", "robert", "98765432109", "maria", false, false),
        new ClientEntity(2L, "stoch", "kamil", "89012345678", "kazimierz", true, false)
    );

    //when
    Mockito.when(clientRepository.findAll()).thenReturn(allClients);
    List<ClientDto> foundClients = clientService.getClients();

    //then
    Assert.assertEquals(2, foundClients.size());
    Assert.assertEquals(allClients.get(0).getPesel(), foundClients.get(0).getPesel());
  }

}
