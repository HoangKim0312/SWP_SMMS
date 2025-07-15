package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class VaccinationRecordRequest {
    @NotNull(message = "Student ID is required")
    private UUID studentId;
    
    @NotNull(message = "Vaccination notice ID is required")
    private Long vaccineNoticeId;
    
    @NotBlank(message = "Results are required")
    private String results;
    
    @NotBlank(message = "Vaccination date is required")
    private LocalDate date;
} 