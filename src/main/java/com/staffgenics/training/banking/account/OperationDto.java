package com.staffgenics.training.banking.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OperationDto {

  private Long id;

  private Long accountId;

  private BigDecimal amount;

  private String operationType;

  private String destinationAccountNumber;

  static OperationDto createInstance(OperationEntity operationEntity){
    OperationDto operationDto = new OperationDto();
    operationDto.setId(operationEntity.getId());
    operationDto.setAccountId(operationEntity.getAccountId());
    operationDto.setAmount(operationEntity.getAmount());
    operationDto.setOperationType(operationEntity.getOperationType().getName());
    operationDto.setDestinationAccountNumber(operationEntity.getDestinationAccountNumber());

    return operationDto;
  }

}
