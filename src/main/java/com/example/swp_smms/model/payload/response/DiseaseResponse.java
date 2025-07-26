package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseResponse {
    private Long diseaseId;
    private String name;
    private int severityLevel;
    private boolean chronic;
    private boolean contagious;
}
