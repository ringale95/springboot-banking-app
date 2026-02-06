package com.ringale.banking_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ringale.banking_app.dto.AccountDto;
import com.ringale.banking_app.entity.Account;
import com.ringale.banking_app.exception.AccountNotFoundException;
import com.ringale.banking_app.exception.InsufficientBalanceException;
import com.ringale.banking_app.exception.InvalidAccountException;
import com.ringale.banking_app.mapper.AccountMapper;
import com.ringale.banking_app.repository.AccountRepository;
import com.ringale.banking_app.service.impl.AccountServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AccountService implementation.
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private AccountServiceImpl accountService;
    
    private Account account;
    private AccountDto accountDto;
    
    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountOwner("John Doe");
        account.setBalance(1000.0);
        
        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setAccountOwner("John Doe");
        accountDto.setBalance(1000.0);
    }
    
    // ========== CREATE ACCOUNT TESTS ==========
    
    @Test
    void testCreateAccountSuccess() {
        // Given
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        
        // When
        AccountDto result = accountService.createAccount(accountDto);
        
        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getAccountOwner());
        assertEquals(1000.0, result.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testCreateAccountWithEmptyOwnerName() {
        // Given
        accountDto.setAccountOwner("");
        
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.createAccount(accountDto));
    }
    
    @Test
    void testCreateAccountWithNullOwnerName() {
        // Given
        accountDto.setAccountOwner(null);
        
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.createAccount(accountDto));
    }
    
    @Test
    void testCreateAccountWithNegativeBalance() {
        // Given
        accountDto.setBalance(-100.0);
        
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.createAccount(accountDto));
    }
    
    // ========== GET ACCOUNT BY ID TESTS ==========
    
    @Test
    void testGetAccountByIdSuccess() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        
        // When
        AccountDto result = accountService.getAccountById(1L);
        
        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getAccountOwner());
        assertEquals(1000.0, result.getBalance());
        verify(accountRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetAccountByIdNotFound() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(999L));
    }
    
    @Test
    void testGetAccountByIdWithInvalidId() {
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.getAccountById(-1L));
        assertThrows(InvalidAccountException.class, () -> accountService.getAccountById(0L));
    }
    
    // ========== DEPOSIT AMOUNT TESTS ==========
    
    @Test
    void testDepositAmountSuccess() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        
        // When
        AccountDto result = accountService.depositAmount(1L, 500.0);
        
        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testDepositAmountWithZeroAmount() {
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.depositAmount(1L, 0.0));
    }
    
    @Test
    void testDepositAmountWithNegativeAmount() {
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.depositAmount(1L, -100.0));
    }
    
    @Test
    void testDepositAmountAccountNotFound() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.depositAmount(999L, 500.0));
    }
    
    // ========== WITHDRAW AMOUNT TESTS ==========
    
    @Test
    void testWithdrawAmountSuccess() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        
        // When
        AccountDto result = accountService.withDrawAmount(1L, 500.0);
        
        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void testWithdrawAmountWithInsufficientBalance() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        
        // When & Then
        assertThrows(InsufficientBalanceException.class, () -> accountService.withDrawAmount(1L, 2000.0));
    }
    
    @Test
    void testWithdrawAmountWithZeroAmount() {
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.withDrawAmount(1L, 0.0));
    }
    
    @Test
    void testWithdrawAmountWithNegativeAmount() {
        // When & Then
        assertThrows(InvalidAccountException.class, () -> accountService.withDrawAmount(1L, -100.0));
    }
    
    @Test
    void testWithdrawAmountAccountNotFound() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.withDrawAmount(999L, 500.0));
    }
}
