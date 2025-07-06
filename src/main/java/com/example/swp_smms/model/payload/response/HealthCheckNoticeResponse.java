package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class HealthCheckNoticeResponse {
    private Long checkNoticeId;
    private String title;
    private String description;
    private String date;
    private String createdAt;
} 