package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VaccinationConfirmationStatusRequest {
    @NotBlank(message = "Status is required")
    private String status; // CONFIRMED, DECLINED, COMPLETED, ONGOING
}
