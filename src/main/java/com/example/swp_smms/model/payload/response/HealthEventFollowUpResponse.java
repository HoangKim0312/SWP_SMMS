package com.example.swp_smms.model.payload.response;

import lombok.Data;
import java.util.UUID;

@Data
public class HealthEventFollowUpResponse {
    private Long followId;
    private Long eventId;
    private UUID parentId;
    private String instruction;
    private Boolean requiresDoctor;
    private String status;
} 