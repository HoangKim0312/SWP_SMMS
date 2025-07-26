package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class AccountFilterRequest {
    private Long classId;
    private String grade;
    private Long roleId;

    private Long diseaseId;
    private Boolean sortByDiseaseSeverity;
    private Boolean isChronic;
    private Boolean isContagious;

    private Long allergenId;
    private Boolean sortByAllergySeverity;
    private Boolean isLifeThreatening;

    private Long conditionId;

    private Integer page = 0;
    private Integer size = 10;
}
