package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VaccinationConfirmationStatusRequest {
    @NotNull(message = "Vaccination confirmation ID is required")
    private Long vaccinationConfirmationId;

    @NotBlank(message = "Status is required")
    private String status; // CONFIRMED, DECLINED, COMPLETED, ONGOING
}
