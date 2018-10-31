package com.staffgenics.training.banking.common;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class NrbNumberValidator {

  public static boolean isNrbNumberValid(String nrb){
    nrb = nrb.replaceAll("[^0-9]+", "");
    if (nrb.length() != 26) {
      return false;
    }
    int[] wagi = IntStream.range(10, 35).toArray();
    //todo: dodac walidacje zagranicznych rachunkow

    nrb = nrb.substring(2) + "2521" + nrb.substring(0, 2);
    if (new BigInteger(nrb).remainder(new BigInteger("97")).equals(new BigInteger("1"))) {
      return true;
    }
    return false;
  }
}
