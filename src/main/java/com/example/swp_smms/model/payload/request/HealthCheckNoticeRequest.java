package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HealthCheckNoticeRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Notice date is required")
    private String date;

    @NotBlank(message = "Priority is required")
    private String priority;

    @NotBlank(message = "Grade is required")
    private Integer grade;
}
