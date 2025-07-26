package com.example.swp_smms.model.payload.response;

import lombok.Data;

@Data
public class MedicationItemResponse {
    private String medicationName;
    private Double amount;
    private String unit;
}
