package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class DiseaseRequest {
    private String name;
    private String description;
    private int severityLevel;
    private boolean chronic;
    private boolean contagious;
}