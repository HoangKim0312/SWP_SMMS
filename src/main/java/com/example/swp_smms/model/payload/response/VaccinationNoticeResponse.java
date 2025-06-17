package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class VaccinationNoticeResponse {
    private Long vaccineNoticeId;
    private String title;
    private String description;
    private String vaccineName;
    private String date;
    private String createdAt;
} 