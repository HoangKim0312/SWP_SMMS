package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MedicationRequest {
    @NotBlank(message = "Medication name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    @NotBlank(message = "Expiry date is required")
    private String expiryDate;
} 