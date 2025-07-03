package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class MedicationItemRequest {
    private String medicationName;
    private Integer amount;
}
