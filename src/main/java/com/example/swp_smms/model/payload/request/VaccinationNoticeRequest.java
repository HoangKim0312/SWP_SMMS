package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VaccinationNoticeRequest {
    private String title;
    private String description;
    private int grade;
    private Long vaccineBatchId;
    private LocalDate vaccinationDate;
    private List<Long> excludeDiseaseIds;
}
