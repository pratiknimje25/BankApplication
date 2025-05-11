package com.javabook.BankApplication.service;

import com.javabook.BankApplication.dto.BankResponse;
import com.javabook.BankApplication.dto.UserRequest;

public interface UserService {

  BankResponse createAccount(UserRequest userRequest);


}
