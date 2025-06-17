package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class VaccinationConfirmationRequest {
    @NotNull(message = "Vaccination notice ID is required")
    private Long vaccineNoticeId;
    
    @NotNull(message = "Student ID is required")
    private UUID studentId;
    
    @NotNull(message = "Parent ID is required")
    private UUID parentId;
    
    @NotBlank(message = "Status is required")
    private String status; // "CONFIRMED", "DECLINED", "PENDING"
} 