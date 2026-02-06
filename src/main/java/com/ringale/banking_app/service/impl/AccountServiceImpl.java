package com.ringale.banking_app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ringale.banking_app.dto.AccountDto;
import com.ringale.banking_app.entity.Account;
import com.ringale.banking_app.mapper.AccountMapper;
import com.ringale.banking_app.repository.AccountRepository;
import com.ringale.banking_app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAcc = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAcc);
    }

    @Override
    public AccountDto getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exists!"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto depositAmount(Long id, double amount) {
        Account account = AccountMapper.mapToAccount(getAccountById(id));
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAcc = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAcc);
    }

    @Override
    public AccountDto withDrawAmount(Long id, double amount) {
        Account account = AccountMapper.mapToAccount(getAccountById(id));
        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAcc = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(account);

    
    }
       
    

}
