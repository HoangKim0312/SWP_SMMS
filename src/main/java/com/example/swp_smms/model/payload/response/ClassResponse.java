package com.example.swp_smms.model.payload.response;

import lombok.Data;

@Data
public class ClassResponse {
    private Long classId;
    private String className;
    private String description;
    private int schoolYear;
}
