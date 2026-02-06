package com.ringale.banking_app.controller;

import java.util.Map;

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
import com.ringale.banking_app.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Add account REST API
    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    // GET Account by ID REST API
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    // Deposit Rest api
    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> depositAmount(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        return new ResponseEntity<>(accountService.depositAmount(id, request.get("amount")), HttpStatus.OK);
    }

    // withdraw rest api
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDto> withDrawAmount(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        return new ResponseEntity<>(accountService.withDrawAmount(id, request.get("amount")), HttpStatus.OK);
    }
}
