package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class VaccinationNoticeResponse {
    private Long vaccineNoticeId;
    private String title;
    private String description;
    private int grade;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private Long vaccineBatchId;
    private List<Long> excludedDiseaseIds;
    private long totalStudentsSentForm;
    private LocalDate createdAt;
}
