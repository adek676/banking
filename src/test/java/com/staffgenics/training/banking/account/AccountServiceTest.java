package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.TestDataProvider;
import com.staffgenics.training.banking.client.ClientEntity;
import com.staffgenics.training.banking.client.ClientRepository;
import com.staffgenics.training.banking.currency.CurrencyRatesEntity;
import com.staffgenics.training.banking.currency.CurrencyRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

  @InjectMocks
  private AccountService accountService;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private CurrencyRepository currencyRepository;

  @Mock
  private ClientRepository clientRepository;

  @Mock
  private PaymentCardService paymentCardService;

  private TestDataProvider testDataProvider = new TestDataProvider();

  private AccountEntity accountEntity = new AccountEntity();

  private ClientEntity clientEntity = new ClientEntity();

  @Test
  public void getAccountsSuccess(){
    //given
    AccountEntity accountEntity1 = testDataProvider.getTestAccountEntity(new BigDecimal(1222));
    AccountEntity accountEntity2 = testDataProvider.getTestAccountEntity(new BigDecimal(9807));
    List<AccountEntity> entities = Arrays.asList(accountEntity1, accountEntity2);
    given(accountRepository.findAll()).willReturn(entities);

    //when
    List<AccountDto> dtos = accountService.getAccounts();

    //then
    assertThat(dtos, hasSize(2));
  }

  @Test
  public void createAccountSuccess(){
    //given
    AccountDto accountDto = testDataProvider.getTestAccountDto(22L,new BigDecimal(0), 22L);
    CurrencyRatesEntity currencyRatesEntity = testDataProvider.getTestCurrency();
    currencyRatesEntity.setCurrencyName("usd");
    accountDto.setCurrency(currencyRatesEntity.getCurrencyName());

    given(clientRepository.findById(Mockito.anyLong())).willReturn(Optional.of(clientEntity));
    given(currencyRepository.findByCurrencyName(accountDto.getCurrency())).willReturn(Optional.of(currencyRatesEntity));
    given(accountRepository.save(Mockito.any(AccountEntity.class))).willAnswer((Answer<AccountEntity>) invocationOnMock -> {
      accountEntity = invocationOnMock.getArgument(0);
      accountEntity.setId(22L);
      return accountEntity;
    });

    //when
    Long id = accountService.createAccount(accountDto);

    //then
    assertEquals(accountDto.getId(), id);
    assertEquals(accountDto.getCurrency(), accountEntity.getCurrency().getCurrencyName());
    assertEquals(new BigDecimal(0),accountEntity.getBalance());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createAccount_FailOnWrongClientId(){
    //given
    AccountDto accountDto = testDataProvider.getTestAccountDto(22L,new BigDecimal(0), 22L);
    given(clientRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

    //when
    Long id = accountService.createAccount(accountDto);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createAccount_FailOnWrongCurrency(){
    //given
    AccountDto accountDto = testDataProvider.getTestAccountDto(22L,new BigDecimal(0), 22L);
    given(clientRepository.findById(Mockito.anyLong())).willReturn(Optional.of(clientEntity));
    given(currencyRepository.findByCurrencyName(accountDto.getCurrency())).willReturn(Optional.empty());

    //when
    Long id = accountService.createAccount(accountDto);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test
  public void deleteAccount_Success(){
    //given
    Long id = 3L;
    PaymentCard paymentCard = new PaymentCard();
    paymentCard.setInactive(false);
    paymentCard.setId(22L);
    accountEntity.setCards(Arrays.asList(paymentCard));

    given(accountRepository.findById(3L)).willReturn(Optional.of(accountEntity));
    given(paymentCardService.deletePaymentCard(paymentCard.getId())).willReturn(true);

    //when
    boolean result = accountService.deleteAccount(id);

    //then
    assertTrue(result);
    assertTrue(accountEntity.isInactive());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteAccount_FailureOnWrongAccountId(){
    //given
    Long id = 33L;
    given(accountRepository.findById(id)).willReturn(Optional.empty());

    //when
    boolean result = accountService.deleteAccount(id);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteAccount_FailureOnTransaction(){
    //given
    Long id = 3L;
    PaymentCard paymentCard = new PaymentCard();
    paymentCard.setInactive(false);
    paymentCard.setId(22L);
    accountEntity.setCards(Arrays.asList(paymentCard));

    given(accountRepository.findById(3L)).willReturn(Optional.of(accountEntity));
    given(paymentCardService.deletePaymentCard(paymentCard.getId())).willThrow(new IllegalArgumentException());

    //when
    boolean result = accountService.deleteAccount(id);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

}
