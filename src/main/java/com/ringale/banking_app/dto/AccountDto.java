package com.ringale.banking_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Account.
 * Includes validation constraints for API input validation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    
    private Long id;
    
    @NotBlank(message = "Account owner name is required")
    private String accountOwner;
    
    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance cannot be negative")
    private double balance;
}
