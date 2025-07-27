package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ClassResponse {
    private Long classId;
    private String className;
    private String description;
    private int schoolYear;
    private int grade;
}
