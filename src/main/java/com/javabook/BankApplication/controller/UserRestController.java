package com.javabook.BankApplication.controller;

import com.javabook.BankApplication.dto.BalanceEnquiry;
import com.javabook.BankApplication.dto.BankResponse;
import com.javabook.BankApplication.dto.CreditDebitRequest;
import com.javabook.BankApplication.dto.UserRequest;
import com.javabook.BankApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/javabank/user")
public class UserRestController {

  @Autowired private UserService userService;

  /**
   * This method is used to create a new user account.
   *
   * @param userRequest the user request object containing user details
   * @return ResponseEntity containing the BankResponse object
   */
  @PostMapping("/createAccount")
  public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest) {

    // Call the createAccount method from the UserService
    BankResponse bankResponse = userService.createAccount(userRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(bankResponse);
  }

  /**
   * This method is used to check the balance of a user account.
   *
   * @param balanceEnquiry the balance enquiry object containing user details
   * @return ResponseEntity containing the BankResponse object
   */
  @PostMapping("/balanceEnquiry")
  public ResponseEntity<?> balanceEnquiry(@RequestBody BalanceEnquiry balanceEnquiry) {

    // Call the balanceEnquiry method from the UserService
    BankResponse bankResponse = userService.balanceEnquiry(balanceEnquiry);
    if (bankResponse.getResponseCode().equals("200")) {
      return ResponseEntity.ok(bankResponse);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bankResponse);
    }
  }

  /**
   * This method is used to credit an amount to a user account.
   *
   * @param creditDebitRequest the credit/debit request object containing user details
   * @return ResponseEntity containing the BankResponse object
   */
  @PostMapping("/creditAmount")
  public ResponseEntity<?> creditAmount(@RequestBody CreditDebitRequest creditDebitRequest) {

    // Call the creditAmount method from the UserService
    BankResponse bankResponse = userService.creditAmount(creditDebitRequest);
    if (bankResponse.getResponseCode().equals("200")) {
      return ResponseEntity.ok(bankResponse);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bankResponse);
    }
  }
}
