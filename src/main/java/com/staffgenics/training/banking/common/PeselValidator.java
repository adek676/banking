package com.staffgenics.training.banking.common;

public class PeselValidator {
  public static boolean isPeselValid(String pesel){
    int[] peselDigits = pesel.chars().map(Character::getNumericValue).toArray();
    int[] wages = {9,7,3,1,9,7,3,1,9,7};

    int sum = 0;
    for (int i = 0; i < peselDigits.length - 1; i++){
      sum += peselDigits[i] * wages[i];
    }
    return peselDigits[peselDigits.length - 1] == sum % 10;
  }

}
