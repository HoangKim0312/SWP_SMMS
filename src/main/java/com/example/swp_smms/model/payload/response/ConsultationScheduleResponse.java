package com.example.swp_smms.model.payload.response;

import com.example.swp_smms.model.enums.ConsultationSlot;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConsultationScheduleResponse {
    private Long consultationId;
    private UUID studentId;
    private UUID parentId;
    private UUID staffId;
    private Long healthCheckRecordId;
    private LocalDate scheduledDate;
    private ConsultationSlot slot;
    private String status;
    private LocalDateTime createdAt;
    private String reason;
} 