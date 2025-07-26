package com.example.swp_smms.model.payload.response;

import lombok.Data;

@Data
public class StudentMedicalSummaryResponse {
    private Long classId;
    private String className;
    private int grade;
    private String fullName;
    private String dob;
    private String gender;
}
