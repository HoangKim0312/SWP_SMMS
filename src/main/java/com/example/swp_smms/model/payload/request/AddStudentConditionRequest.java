package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class AddStudentConditionRequest {
    private String studentId;        // UUID of student
    private Long conditionId;        // Refers to SyndromeDisability
    private String note;             // Any specific observation or comment
}
