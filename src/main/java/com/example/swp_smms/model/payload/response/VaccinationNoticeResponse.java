package com.example.swp_smms.model.payload.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VaccinationNoticeResponse {
    private Long vaccineNoticeId;
    private String title;
    private String description;
    private String vaccineName;
    private LocalDate vaccinationDate; // renamed from "date"
    private LocalDate createdAt;
    private Long batchId;
    private int grade;
}
