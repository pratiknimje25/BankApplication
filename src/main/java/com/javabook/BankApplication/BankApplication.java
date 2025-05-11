package com.javabook.BankApplication;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Tag(
    name = "Bank Application",
    description =
        "This is a simple bank application that allows users to create accounts, check balances, and perform transactions.")
public class BankApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankApplication.class, args);
  }
}
