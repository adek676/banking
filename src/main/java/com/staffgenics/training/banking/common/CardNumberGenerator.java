package com.staffgenics.training.banking.common;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CardNumberGenerator {

  private Random random = new Random();

  public String generate(int len){
    int firstNum = random.nextInt(8) + 1;
    StringBuilder sb = new StringBuilder(firstNum);
    for (int i = 0; i < len - 1; i++){
      sb.append(random.nextInt(9));
    }

    int lastDigit = getCheckNumber(sb.toString());
    sb.append(lastDigit);

    return sb.toString();
  }

  private int getCheckNumber(String num){
    int sum = 0;
    for (int i = 0; i < num.length() - 1; i++){
      int digit = Character.getNumericValue(num.charAt(i));
      if (i % 2 == 0){
        digit = digit * 2;
        if (digit > 9){
          digit = 1 + digit % 10;
        }
      }
      sum += digit;
    }

    return sum * 9 % 10;
  }

  public static void main(String[] args) {
    CardNumberGenerator gen = new CardNumberGenerator();
    System.out.println(gen.generate(16));
  }
}
