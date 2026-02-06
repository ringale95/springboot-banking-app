package com.ringale.banking_app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ringale.banking_app.dto.AccountDto;
import com.ringale.banking_app.entity.Account;
import com.ringale.banking_app.exception.AccountNotFoundException;
import com.ringale.banking_app.exception.InsufficientBalanceException;
import com.ringale.banking_app.exception.InvalidAccountException;
import com.ringale.banking_app.mapper.AccountMapper;
import com.ringale.banking_app.repository.AccountRepository;
import com.ringale.banking_app.service.AccountService;

/**
 * Service implementation for Account operations.
 * Handles business logic, validation, and transaction management.
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new account
     * 
     * @param accountDto - Account data to create
     * @return Created account DTO
     * @throws InvalidAccountException - If account data is invalid
     */
    @Override
    @Transactional
    public AccountDto createAccount(AccountDto accountDto) {
        logger.info("Creating new account for: {}", accountDto.getAccountOwner());
        
        // Validate input
        if (accountDto.getAccountOwner() == null || accountDto.getAccountOwner().trim().isEmpty()) {
            logger.error("Account owner name is required");
            throw new InvalidAccountException("Account owner name cannot be empty");
        }
        
        if (accountDto.getBalance() < 0) {
            logger.error("Initial balance cannot be negative");
            throw new InvalidAccountException("Initial balance cannot be negative");
        }
        
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        
        logger.info("Account created successfully with ID: {}", savedAccount.getId());
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    /**
     * Retrieve account by ID
     * 
     * @param id - Account ID
     * @return Account DTO
     * @throws AccountNotFoundException - If account doesn't exist
     */
    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountById(Long id) {
        logger.info("Fetching account with ID: {}", id);
        
        if (id == null || id <= 0) {
            logger.error("Invalid account ID: {}", id);
            throw new InvalidAccountException("Account ID must be positive");
        }
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Account not found with ID: {}", id);
                    return new AccountNotFoundException("Account not found with ID: " + id);
                });
        
        logger.info("Account retrieved successfully: {}", id);
        return AccountMapper.mapToAccountDto(account);
    }

    /**
     * Deposit amount to account
     * 
     * @param id - Account ID
     * @param amount - Amount to deposit
     * @return Updated account DTO
     * @throws AccountNotFoundException - If account doesn't exist
     * @throws InvalidAccountException - If amount is invalid
     */
    @Override
    @Transactional
    public AccountDto depositAmount(Long id, double amount) {
        logger.info("Processing deposit of {} for account ID: {}", amount, id);
        
        // Validate amount
        if (amount <= 0) {
            logger.error("Invalid deposit amount: {}", amount);
            throw new InvalidAccountException("Deposit amount must be positive");
        }
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Account not found for deposit with ID: {}", id);
                    return new AccountNotFoundException("Account not found with ID: " + id);
                });
        
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        Account savedAccount = accountRepository.save(account);
        
        logger.info("Deposit successful. New balance for account {}: {}", id, newBalance);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    /**
     * Withdraw amount from account
     * 
     * @param id - Account ID
     * @param amount - Amount to withdraw
     * @return Updated account DTO
     * @throws AccountNotFoundException - If account doesn't exist
     * @throws InvalidAccountException - If amount is invalid
     * @throws InsufficientBalanceException - If insufficient balance
     */
    @Override
    @Transactional
    public AccountDto withDrawAmount(Long id, double amount) {
        logger.info("Processing withdrawal of {} from account ID: {}", amount, id);
        
        // Validate amount
        if (amount <= 0) {
            logger.error("Invalid withdrawal amount: {}", amount);
            throw new InvalidAccountException("Withdrawal amount must be positive");
        }
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Account not found for withdrawal with ID: {}", id);
                    return new AccountNotFoundException("Account not found with ID: " + id);
                });
        
        // Check sufficient balance
        if (account.getBalance() < amount) {
            logger.warn("Insufficient balance. Current: {}, Requested: {}", account.getBalance(), amount);
            throw new InsufficientBalanceException(
                    "Insufficient balance. Current balance: " + account.getBalance() + 
                    ", Withdrawal amount: " + amount
            );
        }
        
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        Account savedAccount = accountRepository.save(account);
        
        logger.info("Withdrawal successful. New balance for account {}: {}", id, newBalance);
        return AccountMapper.mapToAccountDto(savedAccount);
    }
}
