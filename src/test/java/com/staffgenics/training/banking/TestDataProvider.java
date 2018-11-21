package com.staffgenics.training.banking;

import com.staffgenics.training.banking.account.*;
import com.staffgenics.training.banking.currency.CurrencyRatesEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class TestDataProvider {

  private final static String VALID_ACCOUNT_NUMBER = "PL31363282532783310725263793";

  public OperationEntity getTestOperationEntity(Long id, Long accountId, OperationTypeEntity operationType,
                                                String destinationAccountNumber, Date date){
    OperationEntity testEntity = new OperationEntity();
    testEntity.setId(id);
    testEntity.setAccountId(accountId);
    testEntity.setOperationType(operationType);
    testEntity.setDestinationAccountNumber(destinationAccountNumber);
    testEntity.setOperationDate(date);
    return testEntity;
  }

  public OperationEntity getTestOperationEntity(){
    return getTestOperationEntity(8L, 88L, new OperationTypeEntity(),"PL11 222222222222",new Date());
  }

  public OperationDto getTestOperationDto(Long id, Long accountId, String operationTypeEntity, String accountNum, BigDecimal amount){
    OperationDto operationDto = new OperationDto();
    operationDto.setId(id);
    operationDto.setAccountId(accountId);
    operationDto.setOperationType(operationTypeEntity);
    operationDto.setDestinationAccountNumber(accountNum);
    operationDto.setAmount(amount);

    return operationDto;
  }

  public OperationDto getTestOperationDto(){
    return getTestOperationDto(99L,999L, "simple", VALID_ACCOUNT_NUMBER, new BigDecimal(123789));
  }

  public CurrencyRatesEntity getTestCurrency(){
    CurrencyRatesEntity currency = new CurrencyRatesEntity();
    currency.setId(5L);
    currency.setConversionRate(new BigDecimal(3.436));
    currency.setCurrencyName("currencyForTest");

    return currency;
  }

  public AccountEntity getTestAccountEntityWithZeroBalance(){
    return getTestAccountEntity(new BigDecimal(0));
  }

  public AccountEntity getTestAccountEntity(BigDecimal balance){
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setId(3L);
    accountEntity.setCurrency(getTestCurrency());
    accountEntity.setBalance(balance);
    accountEntity.setClientId(44L);
    accountEntity.setAccountNumber(VALID_ACCOUNT_NUMBER);

    return accountEntity;
  }

  public AccountDto getTestAccountDto(Long id, BigDecimal balance, Long clientId){
    AccountDto accountDto = new AccountDto();
    accountDto.setId(id);
    accountDto.setBalance(balance);
    accountDto.setClientId(clientId);

    return accountDto;
  }

  public OperationTypeEntity getTestOperationTypeEntity(){
    OperationTypeEntity operationTypeEntity = new OperationTypeEntity();
    operationTypeEntity.setName("test operation");
    operationTypeEntity.setId(33L);
    operationTypeEntity.setIncome(true);

    return operationTypeEntity;
  }

  public PaymentCard getTestPaymentCard(Long id, AccountEntity accountEntity, String number, PaymentCardType cardType){
    PaymentCard paymentCard = new PaymentCard();
    paymentCard.setId(id);
    paymentCard.setType(cardType);
    paymentCard.setAccount(accountEntity);
    paymentCard.setNumber(number);
    paymentCard.setGenerationDate(LocalDate.now());
    paymentCard.setExpirationDate(LocalDate.now().plusYears(10L));

    return paymentCard;
  }

  public PaymentCardType getTestPaymentCardType(Long id, String type){
    PaymentCardType paymentCardType = new PaymentCardType();
    paymentCardType.setId(1L);
    paymentCardType.setCardType(type);
    return paymentCardType;
  }

  public PaymentCardDto getTestPaymentCardDto(Long id, Long acccountId, String paymentCardType){
    PaymentCardDto paymentCardDto = new PaymentCardDto();
    paymentCardDto.setId(id);
    paymentCardDto.setAccountId(acccountId);
    paymentCardDto.setType(paymentCardType);

    return paymentCardDto;
  }

  public String getValidAccountNumber(){
    return VALID_ACCOUNT_NUMBER;
  }

}
