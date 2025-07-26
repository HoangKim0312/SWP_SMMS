package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddAllergyToMedicalProfileRequest {
    private UUID studentId;
    private Long allergenId;
    private String reaction;
    private int severity;
    private boolean lifeThreatening;
}
