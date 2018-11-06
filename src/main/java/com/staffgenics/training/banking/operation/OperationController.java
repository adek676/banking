package com.staffgenics.training.banking.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OperationController {

  private final OperationService operationService;

  @Autowired
  public OperationController(OperationService operationService) {
    this.operationService = operationService;
  }

  @RequestMapping(value = "/operation", method = RequestMethod.POST)
  public Long createOperation(@RequestBody OperationDto operationDto) {
    return operationService.createOperation(operationDto);
  }

  @RequestMapping(value = "/account/{accountId}/operation/{operationId}", method = RequestMethod.GET)
  public OperationDto getOperation(@PathVariable Long accountId, @PathVariable Long operationId) {
    return operationService.getOperation(accountId, operationId);
  }

  @RequestMapping(value = "/account/{id}/operations")
  public List<OperationDto> getOperationWithParams(@PathVariable Long id, @RequestBody OperationFilterDto operationFilterDto){
    return operationService.getOperation(id, operationFilterDto.getMinAmount(),
        operationFilterDto.getMaxAmount(), operationFilterDto.getDateFrom(), operationFilterDto.getDateEnd());
  }
}
