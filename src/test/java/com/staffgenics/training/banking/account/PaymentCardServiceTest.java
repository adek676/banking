package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.TestDataProvider;
import com.staffgenics.training.banking.common.CardNumberGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PaymentCardServiceTest {

  @InjectMocks
  private PaymentCardService paymentCardService;

  @Mock
  private PaymentCardTypeRepository paymentCardTypeRepository;

  @Mock
  private PaymentCardRepository paymentCardRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private CardNumberGenerator cardNumberGenerator = new CardNumberGenerator();

  private TestDataProvider testDataProvider = new TestDataProvider();

  @Test
  public void getPaymentCardsForAccount_SuccessCreate(){
    //given
    AccountEntity accountEntity = testDataProvider.getTestAccountEntityWithZeroBalance();
    PaymentCardType paymentCardType = testDataProvider.getTestPaymentCardType(4L,"testtype");
    PaymentCard paymentCard = testDataProvider.getTestPaymentCard(3L, accountEntity, cardNumberGenerator.generate(16),paymentCardType);
    List<PaymentCard> cards = Arrays.asList(paymentCard);

    given(accountRepository.findById(accountEntity.getId())).willReturn(Optional.of(accountEntity));
    given(paymentCardRepository.findAllByAccountId(accountEntity.getId())).willReturn(cards);

    //when
    List<PaymentCardDto> foundCards = paymentCardService.getPaymentCardsForAccount(accountEntity.getId());
    PaymentCardDto paymentCardDto = foundCards.get(0);

    //then
    assertEquals(cards.size(), foundCards.size());
    assertEquals(paymentCard.getId(), paymentCardDto.getId());
    assertEquals(paymentCard.getAccount().getId(), paymentCardDto.getAccountId());
    assertEquals(paymentCard.getExpirationDate(), paymentCardDto.getExpirationDate());
    assertEquals(paymentCard.getGenerationDate(), paymentCardDto.getGenerationDate());
    assertEquals(paymentCard.getNumber(), paymentCardDto.getNumber());
    assertEquals(paymentCard.getType().getCardType(), paymentCardDto.getType());
  }

  @Test
  public void createPaymentCard_SuccessCreate(){
    //given
    PaymentCard paymentCard = new PaymentCard();
    AccountEntity accountEntity = testDataProvider.getTestAccountEntityWithZeroBalance();
    PaymentCardType paymentCardType = testDataProvider.getTestPaymentCardType(5L,"test type");
    PaymentCardDto paymentCardDto = testDataProvider.getTestPaymentCardDto(3L, accountEntity.getId(), paymentCardType.getCardType());

    given(accountRepository.findById(paymentCardDto.getAccountId())).willReturn(Optional.of(accountEntity));
    given(paymentCardTypeRepository.getAllByCardType(paymentCardDto.getType())).willReturn(Optional.of(paymentCardType));
    given(paymentCardRepository.save(Mockito.any(PaymentCard.class))).willAnswer((Answer<PaymentCard>) invocationOnMock -> {
      paymentCard.setId(44L);
      paymentCard.setType(paymentCardType);
      paymentCard.setGenerationDate(LocalDate.now());
      paymentCard.setExpirationDate(LocalDate.now().plusYears(10L));
      paymentCard.setNumber(cardNumberGenerator.generate(16));
      paymentCard.setAccount(accountEntity);
      return paymentCard;
    });

    //when
    Long id = paymentCardService.createPaymentCard(3L,paymentCardDto);

    //then
    assertEquals(paymentCard.getId(), id);
    assertEquals(paymentCardDto.getType(), paymentCard.getType().getCardType());
    assertEquals(paymentCardDto.getAccountId(), paymentCard.getAccount().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createPayment_FailureOnInvalidCardType(){
    //given
    AccountEntity accountEntity = testDataProvider.getTestAccountEntityWithZeroBalance();
    PaymentCardType paymentCardType = testDataProvider.getTestPaymentCardType(5L,"test type");
    PaymentCardDto paymentCardDto = testDataProvider.getTestPaymentCardDto(3L, accountEntity.getId(), paymentCardType.getCardType());

    given(accountRepository.findById(paymentCardDto.getAccountId())).willReturn(Optional.of(accountEntity));
    given(paymentCardTypeRepository.getAllByCardType(paymentCardDto.getType())).willThrow(new IllegalArgumentException());

    //when
    Long id = paymentCardService.createPaymentCard(3L, paymentCardDto);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createPayment_FailureOnInvalidAccountId(){
    //given
    AccountEntity accountEntity = testDataProvider.getTestAccountEntityWithZeroBalance();
    PaymentCardType paymentCardType = testDataProvider.getTestPaymentCardType(5L,"test type");
    PaymentCardDto paymentCardDto = testDataProvider.getTestPaymentCardDto(3L, accountEntity.getId(), paymentCardType.getCardType());

    given(accountRepository.findById(paymentCardDto.getAccountId())).willThrow(new IllegalArgumentException());

    //when
    Long id = paymentCardService.createPaymentCard(3L, paymentCardDto);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }

  @Test
  public void deletePaymentCard_Success(){
    //given
    Long cardId = 3L;
    PaymentCard paymentCard = new PaymentCard();
    paymentCard.setInactive(false);
    given(paymentCardRepository.findById(cardId)).willReturn(Optional.of(paymentCard));

    //when
    boolean result = paymentCardService.deletePaymentCard(cardId);

    //then
    assertTrue(result);
    assertTrue(paymentCard.isInactive());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deletePaymentCard_FailureOnWrongCardId(){
    //given
    Long id = 2L;
    given(paymentCardRepository.findById(id)).willReturn(Optional.empty());

    //when
    boolean result = paymentCardService.deletePaymentCard(id);

    //then
    Assert.fail("IllegalArgumentException should be thrown");
  }
}
