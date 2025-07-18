package com.example.swp_smms.model.payload.request;

import com.example.swp_smms.model.entity.SyndromeDisability;
import lombok.Data;

@Data
public class SyndromeDisabilityRequest {
    private String name;
    private String description;
    private SyndromeDisability.ConditionType type;
    private int priority;
}