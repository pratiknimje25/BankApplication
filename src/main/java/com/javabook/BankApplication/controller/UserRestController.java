package com.javabook.BankApplication.controller;

import com.javabook.BankApplication.dto.*;
import com.javabook.BankApplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Management Controller", description = "User Management APIs")
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
  @Operation(
      summary = "Create a new user account.",
      description = "This API is used to create a new user account in the bank system.")
  @ApiResponse(responseCode = "201", description = "User account created successfully.")
  @PostMapping("/createAccount")
  public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest) {

    // Call the createAccount method from the UserService
    return new ResponseEntity<>(userService.createAccount(userRequest), HttpStatus.CREATED);
  }

  /**
   * This method is used to check the balance of a user account.
   *
   * @param balanceEnquiry the balance enquiry object containing user details
   * @return ResponseEntity containing the BankResponse object
   */
  @Operation(
      summary = "Check the balance of a user account.",
      description = "This API is used to check the balance of a user account in the bank system.")
  @ApiResponse(responseCode = "200", description = "Balance enquiry successful.")
  @PostMapping("/balanceEnquiry")
  public ResponseEntity<?> balanceEnquiry(@RequestBody BalanceEnquiry balanceEnquiry) {

    // Call the balanceEnquiry method from the UserService
    return new ResponseEntity<>(userService.balanceEnquiry(balanceEnquiry), HttpStatus.OK);
  }

  /**
   * This method is used to credit an amount to a user account.
   *
   * @param creditDebitRequest the credit/debit request object containing user details
   * @return ResponseEntity containing the BankResponse object
   */
  @Operation(
      summary = "Credit an amount to a user account.",
      description = "This API is used to credit an amount to a user account in the bank system.")
  @ApiResponse(responseCode = "200", description = "Amount credited successfully.")
  @PostMapping("/creditAmount")
  public ResponseEntity<?> creditAmount(@RequestBody CreditDebitRequest creditDebitRequest) {

    // Call the creditAmount method from the UserService
    return new ResponseEntity<>(userService.creditAmount(creditDebitRequest), HttpStatus.OK);
  }

  /**
   * This method is used to debit an amount from a user account.
   *
   * @param creditDebitRequest the credit/debit request object containing user details
   * @return ResponseEntity containing the BankResponse object
   */
  @Operation(
      summary = "Debit an amount from a user account.",
      description = "This API is used to debit an amount from a user account in the bank system.")
  @ApiResponse(responseCode = "200", description = "Amount debited successfully.")
  @PostMapping("/debitAmount")
  public ResponseEntity<BankResponse> debitAmount(
      @RequestBody CreditDebitRequest creditDebitRequest) {

    // Call the debitAmount method from the UserService
    return new ResponseEntity<>(userService.debitAmount(creditDebitRequest), HttpStatus.OK);
  }

  /**
   * This method is used to transfer an amount from one user account to another.
   *
   * @param transferRequest the transfer request object containing user details
   * @return ResponseEntity containing the BankResponse object
   */
  @Operation(
      summary = "Transfer an amount from one user account to another.",
      description =
          "This API is used to transfer an amount from one user account to another in the bank system.")
  @ApiResponse(responseCode = "200", description = "Amount transferred successfully.")
  @PostMapping("/transferAmount")
  public ResponseEntity<BankResponse> transferAmount(@RequestBody TransferRequest transferRequest) {

    // Call the transferAmount method from the UserService
    return new ResponseEntity<>(userService.transferAmount(transferRequest), HttpStatus.OK);
  }
}
