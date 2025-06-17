package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.util.UUID;

@Data
public class MedicalProfileResponse {
    private Long medicalProfileId;
    private String allergies;
    private String chronicDiseases;
    private String hearingStatus;
    private String immunizationStatus;
    private String lastUpdated;
    private String pastTreatments;
    private Long recordId;
    private String visionStatusLeft;
    private String visionStatusRight;
    private UUID studentId;
}
