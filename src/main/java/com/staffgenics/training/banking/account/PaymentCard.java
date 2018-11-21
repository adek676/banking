package com.staffgenics.training.banking.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "inactive='false'")
public class PaymentCard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String number;

  @Column(name = "expire")
  private LocalDate expirationDate;

  @Column(name = "generation_date")
  private LocalDate generationDate;

  @ManyToOne
  private AccountEntity account;

  @ManyToOne
  @JoinColumn(name = "card_type_id")
  private PaymentCardType type;

  @Column(name = "inactive")
  private boolean isInactive;

  static PaymentCard createInstance(PaymentCardDto paymentCardDto, AccountEntity accountEntity, PaymentCardType cartType,
                                    String number){
    PaymentCard paymentCard = new PaymentCard();
    paymentCard.setAccount(accountEntity);
    paymentCard.setType(cartType);
    paymentCard.setNumber(number);
    paymentCard.setGenerationDate(LocalDate.now());
    paymentCard.setExpirationDate(LocalDate.now().plusYears(10L));

    return paymentCard;
  }

  void deactivateCard(){
    this.setInactive(true);
  }
}
