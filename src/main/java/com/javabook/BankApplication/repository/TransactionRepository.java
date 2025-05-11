package com.javabook.BankApplication.repository;

import com.javabook.BankApplication.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {}
