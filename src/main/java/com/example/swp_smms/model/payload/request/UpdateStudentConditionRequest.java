package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class UpdateStudentConditionRequest {
    private Long id;       // Primary key of StudentCondition
    private String note;   // New note value
}
