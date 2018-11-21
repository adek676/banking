package com.staffgenics.training.banking.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class PaymentCardDto {

  private Long id;

  private Long accountId;

  private String type;

  private String number;

  private LocalDate expirationDate;

  private LocalDate generationDate;

  static PaymentCardDto createInstance(PaymentCard paymentCard){
    PaymentCardDto paymentCardDto = new PaymentCardDto();
    paymentCardDto.setId(paymentCard.getId());
    paymentCardDto.setAccountId(paymentCard.getAccount().getId());
    paymentCardDto.setType(paymentCard.getType().getCardType());
    paymentCardDto.setExpirationDate(paymentCard.getExpirationDate());
    paymentCardDto.setGenerationDate(paymentCard.getGenerationDate());
    paymentCardDto.setNumber(paymentCard.getNumber());

    return paymentCardDto;
  }
}
