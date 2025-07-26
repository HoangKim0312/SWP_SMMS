package com.example.swp_smms.model.payload.request;

import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class HealthEventApprovalRequest {
    private Long eventId;
    private UUID parentId;
    private HealthEventApprovalStatus approvalStatus; // APPROVED or REJECTED
    private String reason; // Optional reason for rejection or additional notes
} 