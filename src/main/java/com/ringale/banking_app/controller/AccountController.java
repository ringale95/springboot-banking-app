package com.ringale.banking_app.controller;

import java.util.Map;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ringale.banking_app.dto.AccountDto;
import com.ringale.banking_app.dto.ApiResponse;
import com.ringale.banking_app.service.AccountService;

/**
 * REST Controller for Account operations.
 * Provides API endpoints for account management with standardized responses.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Create a new account
     * 
     * @param accountDto - Account data
     * @return Created account with HTTP 201
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AccountDto>> addAccount(@Valid @RequestBody AccountDto accountDto) {
        logger.info("Received request to create account for: {}", accountDto.getAccountOwner());
        AccountDto createdAccount = accountService.createAccount(accountDto);
        
        ApiResponse<AccountDto> response = ApiResponse.success(
                createdAccount,
                "Account created successfully",
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get account by ID
     * 
     * @param id - Account ID
     * @return Account details with HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountDto>> getAccountById(@PathVariable Long id) {
        logger.info("Received request to fetch account with ID: {}", id);
        AccountDto account = accountService.getAccountById(id);
        
        ApiResponse<AccountDto> response = ApiResponse.success(
                account,
                "Account retrieved successfully",
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Deposit amount to account
     * 
     * @param id - Account ID
     * @param request - Request body containing amount
     * @return Updated account with HTTP 200
     */
    @PutMapping("/{id}/deposit")
    public ResponseEntity<ApiResponse<AccountDto>> depositAmount(
            @PathVariable Long id,
            @RequestBody Map<String, Double> request) {
        logger.info("Received deposit request for account ID: {} with amount: {}", id, request.get("amount"));
        
        Double amount = request.get("amount");
        if (amount == null) {
            ApiResponse<AccountDto> response = ApiResponse.error(
                    "Validation failed",
                    "Amount is required in request body",
                    HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        AccountDto updatedAccount = accountService.depositAmount(id, amount);
        
        ApiResponse<AccountDto> response = ApiResponse.success(
                updatedAccount,
                "Amount deposited successfully",
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Withdraw amount from account
     * 
     * @param id - Account ID
     * @param request - Request body containing amount
     * @return Updated account with HTTP 200
     */
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<AccountDto>> withDrawAmount(
            @PathVariable Long id,
            @RequestBody Map<String, Double> request) {
        logger.info("Received withdrawal request for account ID: {} with amount: {}", id, request.get("amount"));
        
        Double amount = request.get("amount");
        if (amount == null) {
            ApiResponse<AccountDto> response = ApiResponse.error(
                    "Validation failed",
                    "Amount is required in request body",
                    HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        AccountDto updatedAccount = accountService.withDrawAmount(id, amount);
        
        ApiResponse<AccountDto> response = ApiResponse.success(
                updatedAccount,
                "Amount withdrawn successfully",
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
