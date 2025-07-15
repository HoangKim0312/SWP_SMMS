package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccineBatchResponse {
    private Long vaccineBatchId;
    private Long vaccineId;
    private LocalDate stockInDate;
    private LocalDate expiryDate;
    private int quantity;
}
