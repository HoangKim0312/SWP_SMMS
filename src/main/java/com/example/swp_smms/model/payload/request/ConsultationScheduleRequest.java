package com.example.swp_smms.model.payload.request;

import com.example.swp_smms.model.enums.ConsultationSlot;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ConsultationScheduleRequest {
    private UUID studentId;
    private UUID parentId;
    private UUID nurseId;
    private Long healthCheckRecordId;
    private LocalDate scheduledDate;
    private ConsultationSlot slot;
    private String reason;
} 