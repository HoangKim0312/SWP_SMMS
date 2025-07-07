package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckConfirmationResponse {
    private Long confirmationId;
    private Long checkNoticeId;
    private UUID studentId;
    private UUID parentId;
    private String status;
    private String confirmedAt;
} 