package com.ringale.banking_app.mapper;

import com.ringale.banking_app.dto.AccountDto;
import com.ringale.banking_app.entity.Account;

/**
 * Mapper for converting between Account entity and AccountDto.
 */
public class AccountMapper {

    /**
     * Convert AccountDto to Account entity
     */
    public static Account mapToAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setAccountOwner(accountDto.getAccountOwner());
        account.setBalance(accountDto.getBalance());
        return account;
    }

    /**
     * Convert Account entity to AccountDto
     */
    public static AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setAccountOwner(account.getAccountOwner());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }
}
