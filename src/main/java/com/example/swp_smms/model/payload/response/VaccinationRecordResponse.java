package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecordResponse {
    private Long recordId;
    private UUID studentId;
    private String studentName;
    private UUID nurseId;
    private String nurseName;
    private Long vaccineNoticeId;
    private String vaccineName;
    private String results;
    private LocalDate date;
} 