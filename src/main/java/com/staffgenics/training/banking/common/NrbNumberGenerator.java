package com.staffgenics.training.banking.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NrbNumberGenerator {
  private Map<Character, Integer> wagesMap = new HashMap<>();
  private String countryCode;

  public NrbNumberGenerator(String countryCode) {
    this.countryCode = countryCode;
    generateMap();
  }

  public String getRandomNrbNumber(){
    Random random = new Random();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 26; i++){
      sb.append(random.nextInt(9));
    }
    return parseInitialNumber(sb.toString());
  }

  private String parseInitialNumber(String initial){
    StringBuilder sbb = new StringBuilder();
    sbb.append(initial.substring(2));
    sbb.append(initial.substring(0,2));
    sbb.append(convertCountryCodeToNumber());

    StringBuilder result = new StringBuilder();
    if (getReminder(sbb.toString()) != 1){
      Integer controlNumber = Integer.valueOf(initial.substring(0,2));
      controlNumber += getReminder(sbb.toString());
      result.append(controlNumber);
      result.append(initial.substring(2));
    }
    return result.toString();
  }

  private int getReminder(String input){
    BigDecimal bigDecimalInput = new BigDecimal(input);
    return Integer.valueOf(bigDecimalInput.remainder(new BigDecimal(97)).toString());
  }

  private String convertCountryCodeToNumber(){
    return wagesMap.get(countryCode.charAt(0)).toString() + wagesMap.get(countryCode.charAt(1));
  }

  private BigDecimal makeDivisibleBy97(BigDecimal input){
    BigDecimal countrySum = new BigDecimal(input.toString().substring(20,24));
    Integer reminder = input.remainder(new BigDecimal(97)).intValue();
    Integer last2 = Integer.valueOf(input.toString().substring(26));

    if (reminder < last2){
      last2 -= reminder;
    }else if (100 - last2 > reminder){
      last2 += reminder;
    }

//    input = input.add(reminder);
    return new BigDecimal(2);
  }

  private void generateMap(){
    int counter = 10;
    for (int i = 0; i < 26; i++){
      wagesMap.put((char)(i + 65), counter++);
    }
  }

  public static void main(String[] args) {
    System.out.println(new NrbNumberGenerator("PL").getRandomNrbNumber());
    System.out.println(NrbNumberValidator.isNrbNumberValid("049553254413776861683302102"));
  }
}
