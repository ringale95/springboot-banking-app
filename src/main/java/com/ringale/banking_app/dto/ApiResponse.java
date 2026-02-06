package com.ringale.banking_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized API response wrapper for all endpoints.
 * Provides consistent response format across the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Response data (can be null for error responses)
     */
    private T data;
    
    /**
     * Timestamp of the response
     */
    private long timestamp;
    
    /**
     * Error details (only for error responses)
     */
    private String error;
    
    /**
     * Create a success response
     */
    public static <T> ApiResponse<T> success(T data, String message, int status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * Create an error response
     */
    public static <T> ApiResponse<T> error(String message, String error, int status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .error(error)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
