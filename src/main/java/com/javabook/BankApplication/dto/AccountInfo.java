package com.javabook.BankApplication.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
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
  private BigDecimal accountBalance;
}
