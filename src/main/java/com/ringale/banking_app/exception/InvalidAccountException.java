package com.ringale.banking_app.exception;

/**
 * Exception thrown for invalid input or business rule violations.
 */
public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException(String message) {
        super(message);
    }

    public InvalidAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
