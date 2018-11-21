package com.staffgenics.training.banking.common;

import org.junit.Assert;
import org.junit.Test;

public class IbanTest {

  @Test
  public void testRandomIban(){
    //given
    Iban ibanGenerator = new Iban("PL");

    //when
    String generatedIban = ibanGenerator.generate();
    boolean isGeneratedIbanValid = NrbNumberValidator.isNrbNumberValid(generatedIban);

    //then
    Assert.assertTrue(isGeneratedIbanValid);
  }
}
