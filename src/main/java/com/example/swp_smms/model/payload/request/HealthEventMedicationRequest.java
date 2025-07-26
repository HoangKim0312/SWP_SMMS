package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class HealthEventMedicationRequest {
    private Long medicationId;
    private Integer dosageAmount;
    private String dosageUnit;
    private String frequency;
    private String duration;
    private String administrationNotes;
    private String usageDate;
    private String usageTime;
} 