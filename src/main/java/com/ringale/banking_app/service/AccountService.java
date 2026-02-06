package com.ringale.banking_app.service;

import com.ringale.banking_app.dto.AccountDto;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    AccountDto depositAmount(Long id, double amount);

    AccountDto withDrawAmount(Long id, double amount);
}
