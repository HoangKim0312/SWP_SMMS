package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;

import java.util.UUID;

@Data
@Builder
public class VaccinationRecordResponse {
    private Long recordId;
    private UUID studentId;
    private String studentName;
    private UUID nurseId;
    private String nurseName;
    private Long vaccineNoticeId;
    private String vaccineName;
    private String results;
    private String date;
} 