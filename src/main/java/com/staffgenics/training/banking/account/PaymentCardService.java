package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.BankingProperties;
import com.staffgenics.training.banking.common.CardNumberGenerator;
import com.staffgenics.training.banking.common.PaymentCardValidator;
import com.staffgenics.training.banking.exceptions.CreditCardAmountExceededException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentCardService {

  private final PaymentCardRepository paymentCardRepository;

  private final PaymentCardTypeRepository paymentCardTypeRepository;

  private final AccountRepository accountRepository;

  private final CardNumberGenerator cardNumberGenerator;

  private final BankingProperties bankingProperties;

  public PaymentCardService(PaymentCardRepository paymentCardRepository, AccountRepository accountRepository, PaymentCardValidator paymentCardValidator, PaymentCardTypeRepository paymentCardTypeRepository, CardNumberGenerator cardNumberGenerator, BankingProperties bankingProperties) {
    this.paymentCardRepository = paymentCardRepository;
    this.accountRepository = accountRepository;
    this.paymentCardTypeRepository = paymentCardTypeRepository;
    this.cardNumberGenerator = cardNumberGenerator;
    this.bankingProperties = bankingProperties;
  }

  List<PaymentCardDto> getPaymentCardsForAccount(Long accountId){
    log.info("Pobieranie wszystkich kart dla konta o id: " + accountId);
    AccountEntity accountEntity = findAccountEntity(accountId);
    return findPaymentCards(accountId).stream().map(PaymentCardDto::createInstance).collect(Collectors.toList());
  }

  @Transactional
  public Long createPaymentCard(Long accountId, PaymentCardDto paymentCardDto){
    log.info("Tworzenie nowej karty");
    AccountEntity accountEntity = findAccountEntity(accountId);
    List<PaymentCard> creditCards = findCreditCardsForAccount(accountId);
    if (creditCards.size() > bankingProperties.getMaxCreditCardAmount()){
      throw new CreditCardAmountExceededException("Podane konto posiada juz maksymalna ilość kart.");
    }
    PaymentCardType cardType = findCardType(paymentCardDto.getType());
    String uniqueCardNumber = getUniqueCardNumber();
    PaymentCard paymentCard = PaymentCard.createInstance(paymentCardDto, accountEntity, cardType, uniqueCardNumber);
    PaymentCard savedPaymentCard = paymentCardRepository.save(paymentCard);
    return savedPaymentCard.getId();
  }

  boolean deletePaymentCard(Long cardId){
    log.info("Dezaktywacja karty o id: " + cardId);
    PaymentCard paymentCard = findCardById(cardId);
    paymentCard.deactivateCard();
    paymentCardRepository.save(paymentCard);
    return true;
  }

  private List<PaymentCard> findCreditCardsForAccount(Long accountId){
    return paymentCardRepository.findByAccountAndType(accountId);
  }

  private List<PaymentCard> findPaymentCards(Long accountId){
    return paymentCardRepository.findAllByAccountId(accountId);
  }

  private AccountEntity findAccountEntity(Long id) {;
    Optional<AccountEntity> accountEntity = accountRepository.findById(id);
    if (!accountEntity.isPresent()) {
      throw new IllegalArgumentException("nie znaleziono encji");
    }
    return accountEntity.get();
  }

  private PaymentCardType findCardType(String type){
    Optional<PaymentCardType> paymentCardTypeOptional = paymentCardTypeRepository.getAllByCardType(type);
    if (!paymentCardTypeOptional.isPresent()){
      throw new IllegalArgumentException("nie znaleziono rodzaju karty");
    }
    return paymentCardTypeOptional.get();
  }

  private PaymentCard findCardById(Long cardId){
    Optional<PaymentCard> paymentCardOptional = paymentCardRepository.findById(cardId);
    if (!paymentCardOptional.isPresent()){
      throw new IllegalArgumentException("Karta o podanym id nie istnieje");
    }
    return paymentCardOptional.get();
  }

  private Optional<PaymentCard> findCardByNumber(String number){
    return paymentCardRepository.findByNumber(number);
  }

  private String getUniqueCardNumber(){
    String result = cardNumberGenerator.generate(16);
    while (findCardByNumber(result).isPresent()){
      result = cardNumberGenerator.generate(16);
    }
    return result;
  }
}
