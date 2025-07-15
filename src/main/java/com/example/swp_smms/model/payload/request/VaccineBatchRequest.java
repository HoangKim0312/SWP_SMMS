package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccineBatchRequest {
    private LocalDate stockInDate;
    private LocalDate expiryDate;
    private int quantity; // NEW field
}
