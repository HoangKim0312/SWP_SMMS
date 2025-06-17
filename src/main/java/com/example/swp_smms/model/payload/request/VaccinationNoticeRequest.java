package com.example.swp_smms.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VaccinationNoticeRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Vaccine name is required")
    private String vaccineName;
    
    @NotBlank(message = "Vaccination date is required")
    private String date;
} 