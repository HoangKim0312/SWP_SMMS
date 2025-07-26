package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class UpdateStudentDiseaseRequest {
    private Long id;           // Primary key of StudentDisease
    private int severity;      // New severity value
}
