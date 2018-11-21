package com.staffgenics.training.banking.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "operation")
public class OperationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long accountId;

  private BigDecimal amount;

  @Column(name = "operationdate")
  private Date operationDate;

  private String destinationAccountNumber;

  @OneToOne
  private OperationTypeEntity operationType;

  static OperationEntity createInstance(Long accountId, OperationDto operationDto){
    Date date = new Date();

    OperationEntity operationEntity = new OperationEntity();
    operationEntity.setAmount(operationDto.getAmount());
    operationEntity.setAccountId(accountId);
    operationEntity.setDestinationAccountNumber(operationDto.getDestinationAccountNumber());
    operationEntity.setOperationDate(date);

    return operationEntity;
  }

}
