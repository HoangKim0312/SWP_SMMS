package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class HealthCheckRecordRequest {
    @NotNull(message = "Health check notice ID is required")
    private Long healthCheckNoticeId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Date is required")
    private String date;
}
