package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.client.ClientEntity;
import com.staffgenics.training.banking.client.ClientRepository;
import com.staffgenics.training.banking.common.Iban;
import com.staffgenics.training.banking.currency.CurrencyRatesEntity;
import com.staffgenics.training.banking.currency.CurrencyRepository;
import com.staffgenics.training.banking.exceptions.AccountBalanceTooLowException;
import com.staffgenics.training.banking.exceptions.AccountNotFoundException;
import com.staffgenics.training.banking.exceptions.ClientNotFoundException;
import com.staffgenics.training.banking.exceptions.InvalidAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {
  private final static String DEFAULT_ACCOUNT_COUNTRY_CODE = "PL";

  private final AccountRepository accountRepository;

  private final CurrencyRepository currencyRepository;

  private final PaymentCardRepository paymentCardRepository;

  private final PaymentCardService paymentCardService;

  private final ClientRepository clientRepository;

  public AccountService(AccountRepository accountRepository, CurrencyRepository currencyRepository, PaymentCardRepository paymentCardRepository, PaymentCardService paymentCardService, ClientRepository clientRepository) {
    this.accountRepository = accountRepository;
    this.currencyRepository = currencyRepository;
    this.paymentCardRepository = paymentCardRepository;
    this.paymentCardService = paymentCardService;
    this.clientRepository = clientRepository;
  }

  List<AccountDto> getAccounts() {
    log.info("Pobieranie wszystkich dostępnych kont");
    return accountRepository.findAll()
        .stream().map(AccountDto::createInstance)
        .collect(Collectors.toList());
  }

  Long createAccount(AccountDto accountDto) {
    log.info("Dodawanie nowego konta");
    ClientEntity clientEntity = findClient(accountDto.getClientId());
    if (!accountDto.getBalance().equals(new BigDecimal(0))) {
      throw new AccountBalanceTooLowException("New account must have balance equal to 0.");
    }
    CurrencyRatesEntity currency = findCurrencyBySymbol(accountDto.getCurrency());
    String accountNumber = new Iban(DEFAULT_ACCOUNT_COUNTRY_CODE).generate();
    AccountEntity accountEntity = AccountEntity.createInstance(accountDto, accountNumber, currency);
    AccountEntity savedAccount = accountRepository.save(accountEntity);
    return savedAccount.getId();
  }

  @Transactional
  public boolean deleteAccount(Long id){
    log.info("Dezaktywacja konta o id: " + id);
    AccountEntity entity = findAccountEntity(id);
    entity.getCards().forEach(paymentCard -> paymentCardService.deletePaymentCard(paymentCard.getId()));
    AccountEntity.deactivate(entity);
    accountRepository.save(entity);
    return true;
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
      throw new AccountNotFoundException("Nie znaleziono encji");
    }
    return accountEntity.get();
  }

  private ClientEntity findClient(Long id){
    Optional<ClientEntity> clientEntityOptional = clientRepository.findById(id);
    if (!clientEntityOptional.isPresent()){
      throw new ClientNotFoundException("Klient o tym id nie isnieje");
    }
    return clientEntityOptional.get();
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