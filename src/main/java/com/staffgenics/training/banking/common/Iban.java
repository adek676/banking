package com.staffgenics.training.banking.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Iban {

  private enum CountryCode{
    PL, DE, RU, GB
  }

  private CountryCode countryCode;
  private Map<Character, Integer> wagesMap = new HashMap<>();

  public Iban(String countryCode) {
    this.countryCode = CountryCode.valueOf(countryCode);

    for (int i = 0; i < 26; i++){
      wagesMap.put((char)(i + 65), i + 10);
    }
  }

  public String generate(){
    return makeDivisible(getRandomNumber());
  }

  private String makeDivisible(String iban){
    if (!isReminder1(new BigDecimal(convertNrbToValidation(iban)))){
      BigDecimal temp = new BigDecimal(iban.substring(2)).add(new BigDecimal(1));
      return makeDivisible(iban.substring(0,2) + temp.toString());
    }
    return iban;
  }

  public String getRandomNumber(){
    Random random = new Random();

    StringBuilder sb = new StringBuilder(countryCode.toString());
    for (int i = 0; i < 26; i++){
      sb.append(random.nextInt(9));
    }
    return sb.toString();
  }

  private String convertNrbToValidation(String initialIban){
    StringBuilder sb = new StringBuilder();
    sb.append(initialIban.substring(4));
    sb.append(wagesMap.get(initialIban.charAt(0)).toString() + wagesMap.get(initialIban.charAt(1)).toString());
    sb.append(initialIban.substring(2,4));
    return sb.toString();
  }

  private boolean isReminder1(BigDecimal number){
    BigDecimal reminderVal = number.remainder(new BigDecimal(97));
    return reminderVal.equals(new BigDecimal(1));
  }

  public static void main(String[] args) {
    Iban iban = new Iban("PL");
  }
}
