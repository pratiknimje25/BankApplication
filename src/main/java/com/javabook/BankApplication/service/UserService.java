package com.javabook.BankApplication.service;

import com.javabook.BankApplication.dto.BalanceEnquiry;
import com.javabook.BankApplication.dto.BankResponse;
import com.javabook.BankApplication.dto.CreditDebitRequest;
import com.javabook.BankApplication.dto.UserRequest;
import com.javabook.BankApplication.entity.User;

public interface UserService {

  BankResponse createAccount(UserRequest userRequest);

  BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry);

  BankResponse creditAmount(CreditDebitRequest request);
}
