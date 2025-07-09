package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckNoticeResponse {
    private Long checkNoticeId;
    private String title;
    private String description;
    private LocalDate date;
    private LocalDateTime createdAt;
} 