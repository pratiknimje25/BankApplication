package com.javabook.BankApplication.repository;

import com.javabook.BankApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  Boolean existsByEmail(String email);

  Boolean existsByAccountNumber(String accountNumber);

  User findByAccountNumber(String accountNumber);
}
