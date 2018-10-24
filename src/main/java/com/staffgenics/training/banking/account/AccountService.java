package com.staffgenics.training.banking.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  List<AccountDto> getAccounts(){
    return accountRepository.findAll()
          .stream().map(AccountDto::createInstance)
          .collect(Collectors.toList());
  }

  Long createAccount(AccountDto accountDto){
    log.info("Dodawanie nowego konta");
    AccountEntity accountEntity = AccountEntity.createInstance(accountDto);
    accountRepository.save(accountEntity);
    return accountEntity.getId();
  }

  AccountDto getAccount(Long id){
    log.info("Pobieranie konta o id: " + id);
    AccountEntity accountEntity = findAccountEntity(id);
    AccountDto accountDto = AccountDto.createInstance(accountEntity);
    return accountDto;
  }

  void editAccount(AccountDto accountDto, Long id){
    log.info("Modyfikacja konta o id: " + id);
    AccountEntity accountEntity = findAccountEntity(id);
    accountEntity.update(accountDto);
    accountRepository.save(accountEntity);
  }

  private AccountEntity findAccountEntity(Long id){
    Optional<AccountEntity> accountEntity = accountRepository.findById(id);
    if (!accountEntity.isPresent()){
      throw new IllegalArgumentException("nie znaleziono encji");
    }
    return accountEntity.get();
  }
}
