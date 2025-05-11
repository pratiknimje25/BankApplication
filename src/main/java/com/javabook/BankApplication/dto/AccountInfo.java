package com.javabook.BankApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class AccountInfo {

  private String accountHolderName;
  private String accountNumber;
  private String accountType;
  private String accountStatus;
  private String accountBalance;
}
