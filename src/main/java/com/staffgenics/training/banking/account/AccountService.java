package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.common.NrbNumberValidator;
import com.staffgenics.training.banking.currency.CurrencyRatesEntity;
import com.staffgenics.training.banking.currency.CurrencyRepository;
import com.staffgenics.training.banking.exceptions.InvalidAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {

  private final AccountRepository accountRepository;

  private final CurrencyRepository currencyRepository;

  public AccountService(AccountRepository accountRepository, CurrencyRepository currencyRepository) {
    this.accountRepository = accountRepository;
    this.currencyRepository = currencyRepository;
  }

  List<AccountDto> getAccounts() {
    return accountRepository.findAll()
        .stream().map(AccountDto::createInstance)
        .collect(Collectors.toList());
  }

  Long createAccount(AccountDto accountDto) {
    log.info("Dodawanie nowego konta");
    if (!accountDto.getBalance().equals(new BigDecimal(0))) {
      throw new InvalidAccountException("New account must have balance equal to 0.");
    }
    if (!NrbNumberValidator.isNrbNumberValid(accountDto.getAccountNumber())) {
      throw new InvalidAccountException("Account number is not valid");
    }
    CurrencyRatesEntity currency = findCurrencyBySymbol(accountDto.getCurrency());
    AccountEntity accountEntity = AccountEntity.createInstance(accountDto);
    accountEntity.setCurrency(currency);

    accountRepository.save(accountEntity);
    return accountEntity.getId();
  }

  AccountDto getAccount(Long id) {
    log.info("Pobieranie konta o id: " + id);
    AccountEntity accountEntity = findAccountEntity(id);
    AccountDto accountDto = AccountDto.createInstance(accountEntity);
    convertBalanceToPln(accountDto, accountEntity.getCurrency());
    return accountDto;
  }

  private AccountEntity findAccountEntity(Long id) {
    Optional<AccountEntity> accountEntity = accountRepository.findById(id);
    if (!accountEntity.isPresent()) {
      throw new IllegalArgumentException("nie znaleziono encji");
    }
    return accountEntity.get();
  }

  private void convertBalanceToPln(AccountDto accountDto, CurrencyRatesEntity currency) {
    accountDto.setBalanceInPln(accountDto.getBalance().multiply(currency.getConversionRate()));
  }

  private CurrencyRatesEntity findCurrencyBySymbol(String currency) {
    Optional<CurrencyRatesEntity> currencyRatesEntityOptional = currencyRepository.findByCurrencyName(currency);
    if (!currencyRatesEntityOptional.isPresent()) {
      throw new IllegalArgumentException("Podana waluta nie istnieje");
    }
    return currencyRatesEntityOptional.get();
  }
}
