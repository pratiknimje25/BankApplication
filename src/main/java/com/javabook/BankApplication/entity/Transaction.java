package com.javabook.BankApplication.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String transactionId;

  private String accountNumber;
  private String transactionType; // "CREDIT" or "DEBIT"
  private BigDecimal amount;
  private String transactionDate;
  private String transactionTime;
  private String status; // "SUCCESS" or "FAILURE"
}
