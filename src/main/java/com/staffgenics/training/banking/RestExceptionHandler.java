package com.staffgenics.training.banking;

import com.staffgenics.training.banking.account.ExceptionMessage;
import com.staffgenics.training.banking.exceptions.*;
import jdk.nashorn.internal.runtime.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({CreditCardAmountExceededException.class, ParserException.class})
  public ResponseEntity<Object> handleCreditCardExceededException(Exception exception, WebRequest request){
    return genericHandle(exception, request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({AccountNotFoundException.class, ClientNotFoundException.class, CurrencyNotFoundException.class})
  public ResponseEntity<Object> handleAccountNotFoundException(Exception exception, WebRequest request){
    return genericHandle(exception, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({InvalidAccountException.class, AccountBalanceTooLowException.class})
  public ResponseEntity<Object> handleNotFoundException(Exception exception, WebRequest request){
    return genericHandle(exception, request, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<Object> genericHandle(Exception exception, WebRequest request, HttpStatus status){
    log.error(exception.getMessage(), exception);

    return handleExceptionInternal(exception,new ExceptionMessage(exception.getMessage()), new HttpHeaders(), status, request);
  }



}