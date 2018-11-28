package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.common.NrbNumberValidator;
import com.staffgenics.training.banking.currency.CurrencyRepository;
import com.staffgenics.training.banking.exceptions.AccountBalanceTooLowException;
import jdk.nashorn.internal.runtime.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

  OperationDto getOperation(Long operationId, Long accountId){
    OperationEntity entity = operationRepository.findByIdAndAccountId(operationId, accountId);
    return OperationDto.createInstance(entity);
  }

  List<OperationDto> getOperation(Long accountId, BigDecimal minAmount, BigDecimal maxAmount, String dateFrom, String dateEnd){
    return operationRepository
            .findOperationEntitiesByParams(accountId, convertToDate(dateFrom), convertToDate(dateEnd), minAmount, maxAmount)
            .stream().map(OperationDto::createInstance).collect(Collectors.toList());
  }

  private Date convertToDate(String dateString){
    SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
    try {
      return formatter.parse(dateString);
    }catch (ParseException e){
      throw new ParserException("Invalid date format");
    }
  }

  @Transactional
  public Long createOperation(Long accountId, OperationDto operationDto) {
    AccountEntity accountEntity = findAccount(accountId);
    if (!NrbNumberValidator.isNrbNumberValid(operationDto.getDestinationAccountNumber())) {
      throw new IllegalArgumentException("Podany numer konta nie jest poprawny");
    }
    OperationTypeEntity operationType = findOperationType(operationDto.getOperationType());

    updateAccountBalance(accountEntity, operationDto.getAmount(), operationType.isIncome());
    OperationEntity operationEntity = OperationEntity.createInstance(accountId, operationDto);
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
      throw new AccountBalanceTooLowException("Brak wystarczających środków na koncie");
    }
  }

  private AccountEntity findAccount(Long accountId) {
    Optional<AccountEntity> accountEntityOptional = accountRepository.findById(accountId);
    if (!accountEntityOptional.isPresent()) {
      throw new IllegalArgumentException("Numer konta o podanym id nie istnieje");
    }
    return accountEntityOptional.get();
//    return accountRepository.getOne(accountId);
  }

  private OperationTypeEntity findOperationType(String name){
    Optional<OperationTypeEntity> optionalOperationTypeEntity =  operationTypeRepository.findOperationTypeEntityByName(name);
    if (!optionalOperationTypeEntity.isPresent()){
      throw new RuntimeException("Nie znaleziono  operacji o podanym typie");
    }
    return optionalOperationTypeEntity.get();
  }
}
