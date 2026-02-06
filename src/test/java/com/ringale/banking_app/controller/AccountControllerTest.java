package com.ringale.banking_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ringale.banking_app.dto.AccountDto;
import com.ringale.banking_app.dto.ApiResponse;
import com.ringale.banking_app.exception.AccountNotFoundException;
import com.ringale.banking_app.service.AccountService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for AccountController.
 */
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    
    @Mock
    private AccountService accountService;
    
    @InjectMocks
    private AccountController accountController;
    
    private AccountDto accountDto;
    
    @BeforeEach
    void setUp() {
        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setAccountOwner("John Doe");
        accountDto.setBalance(1000.0);
    }
    
    // ========== ADD ACCOUNT TESTS ==========
    
    @Test
    void testAddAccountSuccess() {
        // Given
        when(accountService.createAccount(any(AccountDto.class))).thenReturn(accountDto);
        
        // When
        ResponseEntity<ApiResponse<AccountDto>> response = accountController.addAccount(accountDto);
        
        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Account created successfully", response.getBody().getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatus());
    }
    
    // ========== GET ACCOUNT BY ID TESTS ==========
    
    @Test
    void testGetAccountByIdSuccess() {
        // Given
        when(accountService.getAccountById(1L)).thenReturn(accountDto);
        
        // When
        ResponseEntity<ApiResponse<AccountDto>> response = accountController.getAccountById(1L);
        
        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Account retrieved successfully", response.getBody().getMessage());
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
    }
    
    @Test
    void testGetAccountByIdNotFound() {
        // Given
        when(accountService.getAccountById(999L)).thenThrow(new AccountNotFoundException("Account not found"));
        
        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountController.getAccountById(999L));
    }
    
    // ========== DEPOSIT AMOUNT TESTS ==========
    
    @Test
    void testDepositAmountSuccess() {
        // Given
        Map<String, Double> request = new HashMap<>();
        request.put("amount", 500.0);
        when(accountService.depositAmount(1L, 500.0)).thenReturn(accountDto);
        
        // When
        ResponseEntity<ApiResponse<AccountDto>> response = accountController.depositAmount(1L, request);
        
        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Amount deposited successfully", response.getBody().getMessage());
    }
    
    @Test
    void testDepositAmountMissingAmount() {
        // Given
        Map<String, Double> request = new HashMap<>();
        
        // When
        ResponseEntity<ApiResponse<AccountDto>> response = accountController.depositAmount(1L, request);
        
        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
    }
    
    // ========== WITHDRAW AMOUNT TESTS ==========
    
    @Test
    void testWithdrawAmountSuccess() {
        // Given
        Map<String, Double> request = new HashMap<>();
        request.put("amount", 500.0);
        when(accountService.withDrawAmount(1L, 500.0)).thenReturn(accountDto);
        
        // When
        ResponseEntity<ApiResponse<AccountDto>> response = accountController.withDrawAmount(1L, request);
        
        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Amount withdrawn successfully", response.getBody().getMessage());
    }
    
    @Test
    void testWithdrawAmountMissingAmount() {
        // Given
        Map<String, Double> request = new HashMap<>();
        
        // When
        ResponseEntity<ApiResponse<AccountDto>> response = accountController.withDrawAmount(1L, request);
        
        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
    }
}
