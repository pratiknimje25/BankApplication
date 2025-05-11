package com.javabook.BankApplication.service.impl;

import com.javabook.BankApplication.dto.*;
import com.javabook.BankApplication.entity.Transaction;
import com.javabook.BankApplication.entity.User;
import com.javabook.BankApplication.repository.UserRepository;
import com.javabook.BankApplication.service.TransactionService;
import com.javabook.BankApplication.service.UserService;
import com.javabook.BankApplication.utils.AccountNumberGenerator;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private EmailService emailService;

  @Autowired private TransactionService transactionService;

  @Override
  public BankResponse createAccount(UserRequest userRequest) {

    // Check if the email already exists
    if (userRepository.existsByEmail(userRequest.getEmail())) {
      return BankResponse.builder()
          .responseCode("409")
          .responseMessage("Email " + userRequest.getEmail() + " already exists.")
          .build();
    }
    // create a new user using the builder pattern
    User newUser =
        User.builder()
            .id(UUID.randomUUID().toString())
            .firstName(userRequest.getFirstName())
            .lastName(userRequest.getLastName())
            .email(userRequest.getEmail())
            .gender(userRequest.getGender())
            .password(userRequest.getPassword())
            .phoneNumber(userRequest.getPhoneNumber())
            .address(userRequest.getAddress())
            .accountNumber(AccountNumberGenerator.generateAccountNumber())
            .accountBalance(BigDecimal.ZERO)
            .accountType(userRequest.getAccountType())
            .accountStatus("ACTIVE")
            .build();

    User saveUser = userRepository.save(newUser);

    // calling email method to send notification to user
    sendNotificationEmail(saveUser);

    return BankResponse.builder()
        .responseCode(HttpStatus.CREATED.toString())
        .responseMessage("Account created successfully.")
        .accountInfo(
            AccountInfo.builder()
                .accountHolderName(saveUser.getFirstName() + " " + saveUser.getLastName())
                .accountNumber(saveUser.getAccountNumber())
                .accountType(saveUser.getAccountType())
                .accountStatus(saveUser.getAccountStatus())
                .accountBalance(saveUser.getAccountBalance())
                .build())
        .build();
  }

  @Override
  public BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry) {
    // Check if the account number exists
    if (!userRepository.existsByAccountNumber(balanceEnquiry.getAccountNumber())) {
      return BankResponse.builder()
          .responseCode(HttpStatus.NOT_FOUND.getReasonPhrase())
          .responseMessage("Account number " + balanceEnquiry.getAccountNumber() + " not found.")
          .build();
    }
    // Fetch the user by account number
    User userDetails = userRepository.findByAccountNumber(balanceEnquiry.getAccountNumber());

    return BankResponse.builder()
        .responseCode(HttpStatus.OK.getReasonPhrase())
        .responseMessage("Account details retrieved successfully.")
        .accountInfo(
            AccountInfo.builder()
                .accountHolderName(userDetails.getFirstName() + " " + userDetails.getLastName())
                .accountNumber(userDetails.getAccountNumber())
                .accountType(userDetails.getAccountType())
                .accountStatus(userDetails.getAccountStatus())
                .accountBalance(userDetails.getAccountBalance())
                .build())
        .build();
  }

  @Override
  public BankResponse creditAmount(CreditDebitRequest request) {
    // Check if the account number exists

    if (!userRepository.existsByAccountNumber(request.getAccountNumber())) {
      return BankResponse.builder()
          .responseCode(HttpStatus.NOT_FOUND.getReasonPhrase())
          .responseMessage("Account number " + request.getAccountNumber() + " not found.")
          .build();
    }
    // Fetch the user by account number
    User userDetails = userRepository.findByAccountNumber(request.getAccountNumber());
    // Check if the amount is valid
    if (request.getAmount().intValue() <= 0) {
      return BankResponse.builder()
          .responseCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
          .responseMessage("Invalid amount. Amount should be greater than zero.")
          .build();
    }
    // Update the account balance
    userDetails.setAccountBalance(userDetails.getAccountBalance().add(request.getAmount()));
    // Save the updated user
    User updatedUser = userRepository.save(userDetails);

    // send email notification to the user
    EmailDetails amountCreditNotification =
        EmailDetails.builder()
            .recipient(updatedUser.getEmail())
            .msgBody(
                "Dear "
                    + updatedUser.getFirstName()
                    + ",\n\n"
                    + "your account has been created "
                    + request.getAmount()
                    + " amount.")
            .subject("Amount Credit Notification")
            .build();
    emailService.sendEmailNotification(amountCreditNotification);

    // Save the transaction details
    transactionService.createTransaction(
        Transaction.builder()
            .accountNumber(request.getAccountNumber())
            .transactionType("CREDIT")
            .amount(request.getAmount())
            .build());

    // Create the response
    return BankResponse.builder()
        .responseCode(HttpStatus.OK.getReasonPhrase())
        .responseMessage("Amount credited successfully.")
        .accountInfo(
            AccountInfo.builder()
                .accountHolderName(updatedUser.getFirstName() + " " + updatedUser.getLastName())
                .accountNumber(updatedUser.getAccountNumber())
                .accountType(updatedUser.getAccountType())
                .accountStatus(updatedUser.getAccountStatus())
                .accountBalance(updatedUser.getAccountBalance())
                .build())
        .build();
  }

  private void sendNotificationEmail(User saveUser) {
    // Create the email body
    String emailBody =
        "Dear "
            + saveUser.getFirstName()
            + ",\n\n"
            + "Your account has been created successfully.\n"
            + "Account Number: "
            + saveUser.getAccountNumber()
            + "\nAccount Type: "
            + saveUser.getAccountType()
            + "\nAccount Status: "
            + saveUser.getAccountStatus()
            + "\n\nThank you for choosing our bank.\n\nBest regards,\nBank Team";

    String subject = "Account Created Successfully";
    String recipient = saveUser.getEmail();
    EmailDetails emailDetails =
        EmailDetails.builder().recipient(recipient).msgBody(emailBody).subject(subject).build();
    emailService.sendEmailNotification(emailDetails);
  }

  @Override
  public BankResponse debitAmount(CreditDebitRequest request) {

    if (!userRepository.existsByAccountNumber(request.getAccountNumber())) {
      return BankResponse.builder()
          .responseCode(HttpStatus.NOT_FOUND.getReasonPhrase())
          .responseMessage("Account number " + request.getAccountNumber() + " not found.")
          .build();
    }
    // Fetch the user by account number
    User userDetails = userRepository.findByAccountNumber(request.getAccountNumber());
    // Check if the amount is valid
    if (request.getAmount().intValue() <= 0) {
      return BankResponse.builder()
          .responseCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
          .responseMessage("Invalid amount. Amount should be greater than zero.")
          .build();
    }
    // Check if the account has sufficient balance
    if (userDetails.getAccountBalance().compareTo(request.getAmount()) < 0) {
      return BankResponse.builder()
          .responseCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
          .responseMessage("Insufficient balance.")
          .build();
    }
    // Update the account balance
    userDetails.setAccountBalance(userDetails.getAccountBalance().subtract(request.getAmount()));
    // Save the updated user
    User updatedUser = userRepository.save(userDetails);

    // send email notification to the user
    EmailDetails amountDebitNotification =
        EmailDetails.builder()
            .recipient(updatedUser.getEmail())
            .msgBody(
                "Dear "
                    + updatedUser.getFirstName()
                    + ",\n\n"
                    + "your account has been debited "
                    + request.getAmount()
                    + " amount.")
            .subject("Amount Debited Notification")
            .build();
    emailService.sendEmailNotification(amountDebitNotification);

    // Save the debit transaction details
    transactionService.createTransaction(
        Transaction.builder()
            .accountNumber(request.getAccountNumber())
            .transactionType("DEBIT")
            .amount(request.getAmount())
            .build());

    // Create the response
    return BankResponse.builder()
        .responseCode(HttpStatus.OK.getReasonPhrase())
        .responseMessage("Amount debited successfully.")
        .accountInfo(
            AccountInfo.builder()
                .accountHolderName(updatedUser.getFirstName() + " " + updatedUser.getLastName())
                .accountNumber(updatedUser.getAccountNumber())
                .accountType(updatedUser.getAccountType())
                .accountStatus(updatedUser.getAccountStatus())
                .accountBalance(updatedUser.getAccountBalance())
                .build())
        .build();
  }

  @Override
  public BankResponse transferAmount(TransferRequest transferRequest) {
    // Check if the sender account number exists
    if (!userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber())) {
      return BankResponse.builder()
          .responseCode(HttpStatus.NOT_FOUND.getReasonPhrase())
          .responseMessage(
              "Source account number " + transferRequest.getSourceAccountNumber() + " not found.")
          .build();
    }
    // Check if the receiver account number exists
    if (!userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber())) {
      return BankResponse.builder()
          .responseCode(HttpStatus.NOT_FOUND.getReasonPhrase())
          .responseMessage(
              "Destination account number "
                  + transferRequest.getDestinationAccountNumber()
                  + " not found.")
          .build();
    }
    // Fetch the sender and receiver by account number
    User sourceAccount =
        userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
    User destinationAccount =
        userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
    // Check if the amount is valid
    if (transferRequest.getAmount().intValue() <= 0) {
      return BankResponse.builder()
          .responseCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
          .responseMessage("Invalid amount. Amount should be greater than zero.")
          .build();
    }
    // Check if the sender account has sufficient balance
    if (sourceAccount.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
      return BankResponse.builder()
          .responseCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
          .responseMessage("Insufficient balance in source account.")
          .build();
    }
    // Update the sender account balance
    sourceAccount.setAccountBalance(
        sourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
    // Update the receiver account balance
    destinationAccount.setAccountBalance(
        destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
    // Save the updated sender and receiver accounts
    userRepository.save(sourceAccount);
    userRepository.save(destinationAccount);
    // Create the response
    return BankResponse.builder()
        .responseCode(HttpStatus.OK.getReasonPhrase())
        .responseMessage("Amount transferred successfully.")
        .accountInfo(
            AccountInfo.builder()
                .accountHolderName(sourceAccount.getFirstName() + " " + sourceAccount.getLastName())
                .accountNumber(sourceAccount.getAccountNumber())
                .accountType(sourceAccount.getAccountType())
                .accountStatus(sourceAccount.getAccountStatus())
                .accountBalance(sourceAccount.getAccountBalance())
                .build())
        .build();
  }
}
