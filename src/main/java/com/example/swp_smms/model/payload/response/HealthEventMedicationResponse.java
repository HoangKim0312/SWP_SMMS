package com.example.swp_smms.model.payload.response;

import lombok.Data;

@Data
public class HealthEventMedicationResponse {
    private Long healthEventMedicationId;
    private Long medicationId;
    private String medicationName;
    private String medicationDescription;
    private Integer dosageAmount;
    private String dosageUnit;
    private String frequency;
    private String duration;
    private String administrationNotes;
    private String usageDate;
    private String usageTime;
} 