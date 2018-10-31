package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.common.PeselValidator;
import org.junit.Assert;
import org.junit.Test;

public class PeselValidatorTest {

  @Test
  public void testValidPesel(){
    //given
    String validPesel = "92062100778";

    //when
    boolean isValid = PeselValidator.isPeselValid(validPesel);

    //then
    Assert.assertTrue(isValid);
  }
}
