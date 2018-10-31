package com.staffgenics.training.banking.account;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

  private AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @RequestMapping(value = "/accounts", method = RequestMethod.GET)
  public List<AccountDto> getAccounts(){
    return accountService.getAccounts();
  }

  @RequestMapping(value = "/account", method = RequestMethod.POST)
  public Long createAccount(@RequestBody AccountDto accountDto){
    return accountService.createAccount(accountDto);
  }

  @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
  public AccountDto getAccount(@PathVariable Long id){
    return accountService.getAccount(id);
  }
}
