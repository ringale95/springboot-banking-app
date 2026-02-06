package com.ringale.banking_app.mapper;

import com.ringale.banking_app.dto.AccountDto;
import com.ringale.banking_app.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(AccountDto accountDto) {
        Account account = new Account(
                accountDto.getId(),
                accountDto.getAccountOwner(),
                accountDto.getBalance());
        return account;
    }

    public static AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto(
            account.getId(),
            account.getAccountOwner(),
            account.getBalance()
        );
        return accountDto;
    }
}
