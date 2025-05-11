package com.javabook.BankApplication.service.impl;

import com.javabook.BankApplication.dto.AccountInfo;
import com.javabook.BankApplication.dto.BankResponse;
import com.javabook.BankApplication.dto.EmailDetails;
import com.javabook.BankApplication.dto.UserRequest;
import com.javabook.BankApplication.entity.User;
import com.javabook.BankApplication.repository.UserRepository;
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
                .accountBalance(String.valueOf(saveUser.getAccountBalance()))
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
}
