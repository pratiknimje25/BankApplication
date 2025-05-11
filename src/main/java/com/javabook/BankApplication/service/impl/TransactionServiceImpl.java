package com.javabook.BankApplication.service.impl;

import com.javabook.BankApplication.entity.Transaction;
import com.javabook.BankApplication.repository.TransactionRepository;
import com.javabook.BankApplication.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  public void createTransaction(Transaction transaction) {

    transaction.setStatus("SUCCESS");
    transaction.setTransactionDate(java.time.LocalDate.now().toString());
    transaction.setTransactionTime(java.time.LocalTime.now().toString());
    transactionRepository.save(transaction);
  }
}
