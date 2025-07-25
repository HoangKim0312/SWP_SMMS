package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckRecordResponse {
    private Long recordId;
    private UUID studentId;
    private UUID nurseId;
    private Long checkNoticeId;
    private String results;
    private LocalDate date;
    private Long medicalProfileId;
} 