package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class UpdateStudentAllergyRequest {
    private Long studentAllergyId;       // required
    private String reaction;
    private int severity;
    private boolean isLifeThreatening;
}
