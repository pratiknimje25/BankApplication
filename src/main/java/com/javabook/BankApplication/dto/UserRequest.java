package com.javabook.BankApplication.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

  private String firstName;
  private String lastName;
  private String email;
  private String gender;
  private String password;
  private String phoneNumber;
  private String address;
  private String accountType;
}
