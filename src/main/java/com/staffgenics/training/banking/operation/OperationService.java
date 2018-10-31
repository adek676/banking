package com.staffgenics.training.banking.operation;

import com.staffgenics.training.banking.account.AccountEntity;
import com.staffgenics.training.banking.account.AccountRepository;
import com.staffgenics.training.banking.common.NrbNumberValidator;
import com.staffgenics.training.banking.currency.CurrencyRatesEntity;
import com.staffgenics.training.banking.currency.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OperationService {

  private final OperationRepository operationRepository;
  private final AccountRepository accountRepository;
  private final OperationTypeRepository operationTypeRepository;
  private final CurrencyRepository currencyRepository;

  @Autowired
  public OperationService(OperationRepository operationRepository, AccountRepository accountRepository,
                          OperationTypeRepository operationTypeRepository, CurrencyRepository currencyRepository) {
    this.operationRepository = operationRepository;
    this.accountRepository = accountRepository;
    this.operationTypeRepository = operationTypeRepository;
    this.currencyRepository = currencyRepository;
  }

  OperationDto getOperation(Long accountId, Long operationId){
    OperationEntity entity = operationRepository.findByIdAndAccountId(operationId, accountId);
    return OperationDto.createInstance(entity);
  }

  Long createOperation(OperationDto operationDto) {
    if (!NrbNumberValidator.isNrbNumberValid(operationDto.getDestinationAccountNumber())) {
      throw new IllegalArgumentException("Podany numer konta nie jest poprawny");
    }
    AccountEntity accountEntity = findAccount(operationDto.getAccountId());
    OperationTypeEntity operationType = findOperationType(operationDto.getOperationType());

    updateAccountBalance(accountEntity, operationDto.getAmount(), operationType.isIncome());
    OperationEntity operationEntity = OperationEntity.createInstance(operationDto);
    operationEntity.setOperationType(operationType);

    accountRepository.save(accountEntity);
    operationRepository.save(operationEntity);

    return operationEntity.getId();
  }

  private void updateAccountBalance(AccountEntity accountEntity, BigDecimal amount, boolean isIncome) {
    BigDecimal currentBalance = accountEntity.getBalance();
    if (isIncome) {
      accountEntity.setBalance(currentBalance.add(amount));
      return;
    }
    if (currentBalance.compareTo(amount) >= 0) {
      accountEntity.setBalance(currentBalance.subtract(amount));
    } else {
      throw new IllegalArgumentException("Brak wystarczających środków na koncie");
    }
  }

  private AccountEntity findAccount(Long accountId) {
    Optional<AccountEntity> accountEntityOptional = accountRepository.findById(accountId);
    if (!accountEntityOptional.isPresent()) {
      throw new IllegalArgumentException("Numer konta o podanym id nie istnieje");
    }
    return accountEntityOptional.get();
  }

  private OperationTypeEntity findOperationType(String name){
    Optional<OperationTypeEntity> optionalOperationTypeEntity =  operationTypeRepository.findOperationTypeEntityByName(name);
    if (!optionalOperationTypeEntity.isPresent()){
      throw new RuntimeException("Nie znaleziono  operacji o podanym typie");
    }
    return optionalOperationTypeEntity.get();
  }
}
