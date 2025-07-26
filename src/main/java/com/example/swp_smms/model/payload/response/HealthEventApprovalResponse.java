package com.example.swp_smms.model.payload.response;

import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class HealthEventApprovalResponse {
    private Long eventId;
    private UUID parentId;
    private String parentName;
    private HealthEventApprovalStatus approvalStatus;
    private String reason;
    private LocalDateTime approvalDate;
    private String studentName;
    private String eventDescription;
    private String message;
} 