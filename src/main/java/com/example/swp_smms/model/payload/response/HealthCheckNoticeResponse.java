package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckNoticeResponse {
    private Long checkNoticeId;
    private String title;
    private String description;
    private String date;
    private String createdAt;
} 