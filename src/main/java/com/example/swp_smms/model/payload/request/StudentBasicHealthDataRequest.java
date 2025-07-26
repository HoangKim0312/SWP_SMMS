package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.util.UUID;

@Data
public class StudentBasicHealthDataRequest {
    private UUID studentId;
    private Double heightCm;
    private Double weightKg;
    private String visionLeft;
    private String visionRight;
    private String hearingStatus;
    private String gender;
    private String bloodType;
}
