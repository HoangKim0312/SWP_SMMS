package com.example.swp_smms.model.payload.request;

import lombok.Data;
import java.util.UUID;

@Data
public class HealthEventFollowUpRequest {
    private Long eventId;
    private UUID parentId;
    private String instruction;
    private Boolean requiresDoctor;
    private String status;
} 