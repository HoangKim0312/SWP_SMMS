package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.util.UUID;

@Data
public class MedicalProfileRequest {
    private String allergies;
    private String chronicDiseases;
    private String hearingStatus;
    private String immunizationStatus;
    private String lastUpdated;
    private String pastTreatments;
    private String visionStatus;
}

