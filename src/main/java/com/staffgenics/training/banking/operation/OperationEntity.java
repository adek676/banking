package com.staffgenics.training.banking.operation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

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

  private String destinationAccountNumber;

  @OneToOne
  private OperationTypeEntity operationType;

  static OperationEntity createInstance(OperationDto operationDto){
    OperationEntity operationEntity = new OperationEntity();
    operationEntity.setAmount(operationDto.getAmount());
    operationEntity.setAccountId(operationDto.getAccountId());
    operationEntity.setDestinationAccountNumber(operationDto.getDestinationAccountNumber());

    return operationEntity;
  }

}
