package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class MedicationResponse {
    private Long medicationId;
    private String name;
    private String description;
    private Integer quantity;
    private String expiryDate;
} 