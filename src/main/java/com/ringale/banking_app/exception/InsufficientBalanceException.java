package com.ringale.banking_app.exception;

/**
 * Exception thrown when there are insufficient funds for withdrawal.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
