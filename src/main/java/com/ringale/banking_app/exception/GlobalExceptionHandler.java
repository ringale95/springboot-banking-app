package com.ringale.banking_app.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ringale.banking_app.dto.ApiResponse;

/**
 * Global exception handler for the entire application.
 * Handles custom exceptions and provides standardized error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle AccountNotFoundException
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleAccountNotFoundException(
            AccountNotFoundException ex, WebRequest request) {
        logger.warn("Account not found: {}", ex.getMessage());
        
        ApiResponse<?> response = ApiResponse.error(
                "Account not found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle InsufficientBalanceException
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<?>> handleInsufficientBalanceException(
            InsufficientBalanceException ex, WebRequest request) {
        logger.warn("Insufficient balance: {}", ex.getMessage());
        
        ApiResponse<?> response = ApiResponse.error(
                "Insufficient balance",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle InvalidAccountException
     */
    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidAccountException(
            InvalidAccountException ex, WebRequest request) {
        logger.warn("Invalid account: {}", ex.getMessage());
        
        ApiResponse<?> response = ApiResponse.error(
                "Invalid account data",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation error: {}", ex.getMessage());
        
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.append(error.getDefaultMessage()).append("; ")
        );
        
        ApiResponse<?> response = ApiResponse.error(
                "Validation failed",
                errors.toString(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(
            Exception ex, WebRequest request) {
        logger.error("An unexpected error occurred", ex);
        
        ApiResponse<?> response = ApiResponse.error(
                "An unexpected error occurred",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
