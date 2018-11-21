package com.staffgenics.training.banking.account;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

  private AccountService accountService;

  private PaymentCardService paymentCardService;

  private OperationService operationService;

  public AccountController(AccountService accountService, PaymentCardService paymentCardService, OperationService operationService) {
    this.accountService = accountService;
    this.paymentCardService = paymentCardService;
    this.operationService = operationService;
  }

  @RequestMapping(value = "/accounts", method = RequestMethod.GET)
  public List<AccountDto> getAccounts(){
    return accountService.getAccounts();
  }

  @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
  public AccountDto getAccount(@PathVariable Long id){
    return accountService.getAccount(id);
  }

  @RequestMapping(value = "/account", method = RequestMethod.POST)
  public Long createAccount(@RequestBody AccountDto accountDto){
    return accountService.createAccount(accountDto);
  }

  @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
  public boolean deleteAccount(@PathVariable Long id){
    return accountService.deleteAccount(id);
  }

  @RequestMapping(value = "/account/{id}/cards", method = RequestMethod.GET)
  public List<PaymentCardDto> getPaymentCard(@PathVariable Long id){
    return paymentCardService.getPaymentCardsForAccount(id);
  }

  @RequestMapping(value = "/account/{accountId}/card", method = RequestMethod.POST)
  public Long createPaymentCard(@PathVariable Long accountId,@RequestBody PaymentCardDto paymentCardDto){
    return paymentCardService.createPaymentCard(accountId, paymentCardDto);
  }

  @RequestMapping(value = "/account/{id}/operation", method = RequestMethod.POST)
  public Long createOperation(@PathVariable Long id, @RequestBody OperationDto operationDto) {
    return operationService.createOperation(id, operationDto);
  }

  @RequestMapping(value = "/account/{accountId}/operation/{operationId}", method = RequestMethod.GET)
  public OperationDto getOperation(@PathVariable Long accountId, @PathVariable Long operationId) {
    return operationService.getOperation(operationId, accountId);
  }

  @RequestMapping(value = "/account/{id}/operations", method = RequestMethod.POST)
  public List<OperationDto> getOperationWithParams(@PathVariable Long id, @RequestBody OperationFilterDto operationFilterDto){
    return operationService.getOperation(id, operationFilterDto.getMinAmount(),
        operationFilterDto.getMaxAmount(), operationFilterDto.getDateFrom(), operationFilterDto.getDateEnd());
  }
}
