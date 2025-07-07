package com.example.swp_smms.model.payload.response;

import com.example.swp_smms.model.enums.MedicationType;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationResponse {
    private Long medicationId;
    private String name;
    private String description;
    private Integer quantity;
    private String quantityType;
    private MedicationType medicationType;
    private String expiryDate;
} 