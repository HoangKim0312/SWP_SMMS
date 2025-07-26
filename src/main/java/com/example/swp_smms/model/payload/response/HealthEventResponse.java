package com.example.swp_smms.model.payload.response;

import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import com.example.swp_smms.model.enums.HealthEventPriority;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class HealthEventResponse {
    private Long eventId;
    private String eventDate;
    private String eventType;
    private String description;
    private String solution;
    private String note;
    private String status;
    private UUID studentID;
    private UUID nurseID;
    private HealthEventPriority priority;
    private HealthEventApprovalStatus parentApprovalStatus;
    private String parentApprovalReason;
    private LocalDateTime parentApprovalDate;
    private UUID approvedByParentID;
    private String approvedByParentName;
    private Boolean requiresHomeCare;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<HealthEventMedicationResponse> medications;
    private List<HealthEventFollowUpResponse> followUps;
}
