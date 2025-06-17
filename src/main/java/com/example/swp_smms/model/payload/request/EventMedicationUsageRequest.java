package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EventMedicationUsageRequest {
    @NotNull(message = "Health event ID is required")
    private Long healthEventId;
    
    @NotNull(message = "Medication ID is required")
    private Long medicationId;
    
    @NotNull(message = "Quantity used is required")
    @Positive(message = "Quantity used must be positive")
    private Integer quantityUsed;
    
    private String time;
    
    private String dosage;
} 