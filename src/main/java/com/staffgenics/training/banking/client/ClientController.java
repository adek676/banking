package com.staffgenics.training.banking.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler obsługujący klientów.
 */
@RestController
public class ClientController {

  private final ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @RequestMapping(value = "/clients", method = RequestMethod.GET)
  public List<ClientDto> getClients() {
    return clientService.getClients();
  }

  @RequestMapping(value = "/client", method = RequestMethod.POST)
  public Long createClient(@RequestBody ClientDto clientDto) {
    return clientService.createClient(clientDto);
  }

  @RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
  public ClientDto getClient(@PathVariable Long id) {
    return clientService.getClient(id);
  }

  @RequestMapping(value = "/client/find", method = RequestMethod.POST)
  public ClientDto getClientWithParams(@RequestBody ClientCriteria clientCriteria) {
    return clientService.getClientByParams(clientCriteria.getName(), clientCriteria.getSurname(), clientCriteria.isVip());
  }

  @RequestMapping(value = "/client/pesel/{pesel}", method = RequestMethod.GET)
  public ClientDto getClientByPesel(@PathVariable String pesel){
    return clientService.getClientByPesel(pesel);
  }

  @RequestMapping(value = "/client/{id}", method = RequestMethod.PUT)
  public void createClient(@RequestBody ClientDto clientDto, @PathVariable Long id) {
    clientService.editClient(clientDto, id);
  }

  @RequestMapping(value = "/client/{clientId}", method = RequestMethod.DELETE)
  public boolean deleteClient(@PathVariable Long clientId){
    return clientService.deleteClient(clientId);
  }
}
