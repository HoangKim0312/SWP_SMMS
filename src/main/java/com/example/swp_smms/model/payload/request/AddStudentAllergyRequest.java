package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddStudentAllergyRequest {
    private UUID studentId; // or use medicalProfileId if you prefer
    private Long allergenId;
    private String reaction;
    private int severity;
    private boolean isLifeThreatening;
}
