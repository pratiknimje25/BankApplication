package com.javabook.BankApplication.utils;

import java.time.Year;

public class AccountNumberGenerator {

  /*
   *  This method generates a random 10-digit account number.
   *   year + 10 digit random number
   */

  public static String generateAccountNumber() {
    String year = String.valueOf(Year.now().getValue());
    String randomDigits = String.valueOf((int) (Math.random() * 1000000000));
    return year + randomDigits;
  }
}
