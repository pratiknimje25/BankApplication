package com.javabook.BankApplication.service;

import com.javabook.BankApplication.dto.*;
import com.javabook.BankApplication.entity.User;

public interface UserService {

  BankResponse createAccount(UserRequest userRequest);

  BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry);

  BankResponse creditAmount(CreditDebitRequest request);

  BankResponse debitAmount(CreditDebitRequest request);

  BankResponse transferAmount(TransferRequest request);
}
