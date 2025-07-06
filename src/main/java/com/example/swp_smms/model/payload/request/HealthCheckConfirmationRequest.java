package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class HealthCheckConfirmationRequest {
    @NotNull(message = "Health check notice ID is required")
    private Long healthCheckNoticeId;

    @NotNull(message = "Student ID is required")
    private UUID studentId;

    @NotNull(message = "Parent ID is required")
    private UUID parentId;

    @NotBlank(message = "Status is required")
    private String status;

    
}
