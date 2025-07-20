package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class AddStudentDiseaseRequest {
    private String studentId;       // UUID string
    private Long diseaseId;         // Existing disease from reference table
    private String sinceDate;       // Format: YYYY-MM-DD
    private int severity;           // 1–10
}
