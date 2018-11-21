package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.TestDataProvider;
import com.staffgenics.training.banking.common.Iban;
import com.staffgenics.training.banking.currency.CurrencyRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {

  @InjectMocks
  private OperationService operationService;

  @Mock
  private Iban ibanGenerator;

  @Mock
  private OperationRepository operationRepository;

  @Mock
  private CurrencyRepository currencyRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private OperationTypeRepository operationTypeRepository;

  private TestDataProvider testDataProvider = new TestDataProvider();

  @Test
  public void getOperationExisting() {
    //given
    OperationTypeEntity operationTypeEntity = new OperationTypeEntity();
    operationTypeEntity.setName("operation");
    OperationEntity operationEntity = testDataProvider.getTestOperationEntity(1L, 2L, operationTypeEntity, ibanGenerator.generate(), new Date());
    given(operationRepository.findByIdAndAccountId(1L,2L)).willReturn(operationEntity);

    //when
    OperationDto operationFound = operationService.getOperation(1L,2L);

    //then
    then(operationRepository).should(times(1)).findByIdAndAccountId(1L,2L);
    assertEquals(operationEntity.getAccountId(), operationFound.getAccountId());
    assertEquals(operationEntity.getAmount(), operationFound.getAmount());
    assertEquals(operationEntity.getOperationType().getName(), operationFound.getOperationType());
    assertEquals(operationEntity.getDestinationAccountNumber(), operationFound.getDestinationAccountNumber());
  }

  @Test
  public void createOperationSuccessAddMoney() {
    //given
    Long accountId = 2L;
    OperationEntity operationEntity = testDataProvider.getTestOperationEntity();
    OperationDto operationDto = testDataProvider.getTestOperationDto();
    AccountEntity accountEntity = testDataProvider.getTestAccountEntityWithZeroBalance();
    OperationTypeEntity operationTypeEntity = testDataProvider.getTestOperationTypeEntity();

    when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
    when(operationTypeRepository.findOperationTypeEntityByName(operationDto.getOperationType())).thenReturn(Optional.of(operationTypeEntity));
    when(operationRepository.save(Mockito.any(OperationEntity.class))).thenReturn(operationEntity);

    //when
    Long id = operationService.createOperation(accountId,operationDto);

    //then
    assertEquals(operationDto.getAmount(),accountEntity.getBalance());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createOperationInvalidOperationAmount(){
    //given
    Long accountId = 2L;
    OperationDto operationDto = testDataProvider.getTestOperationDto();
    OperationTypeEntity operationTypeEntity = testDataProvider.getTestOperationTypeEntity();
    operationTypeEntity.setIncome(false);

    //when
    Long id = operationService.createOperation(accountId, operationDto);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createOperationInvalidAccountNumber(){
    //given
    Long accountId = 2L;
    OperationDto operationDto = testDataProvider.getTestOperationDto();
    operationDto.setDestinationAccountNumber("12345");
    AccountEntity accountEntity = testDataProvider.getTestAccountEntityWithZeroBalance();

    when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));

    //when
    Long id = operationService.createOperation(accountId, operationDto);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createOperationInvalidAccountId(){
    //given
    Long accountId = 2L;
    OperationDto operationDto = testDataProvider.getTestOperationDto();

    //when
    Long id = operationService.createOperation(accountId, operationDto);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createOperationRollbackTransactionOnError(){
    //given
    Long accountId = 2L;
    BigDecimal ACCOUNT_BALANCE = new BigDecimal(500);
    OperationDto operationDto = testDataProvider.getTestOperationDto();
    AccountEntity accountEntity = testDataProvider.getTestAccountEntity(ACCOUNT_BALANCE);
    OperationTypeEntity operationTypeEntity = testDataProvider.getTestOperationTypeEntity();

    when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
    when(operationRepository.save(Mockito.any(OperationEntity.class))).thenThrow(new IllegalArgumentException());
    when(operationTypeRepository.findOperationTypeEntityByName(operationDto.getOperationType())).thenReturn(Optional.of(operationTypeEntity));

    //when
    Long id = operationService.createOperation(accountId, operationDto);

    //then
    Assert.fail("Illegal argument exception should be thrown");
  }
}