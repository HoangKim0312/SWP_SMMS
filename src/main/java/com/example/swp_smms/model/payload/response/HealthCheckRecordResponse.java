package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;

import java.util.UUID;

@Data
@Builder
public class HealthCheckRecordResponse {
    private Long recordId;
    private UUID studentId;
    private UUID nurseId;
    private Long checkNoticeId;
    private String results;
    private String date;
} 