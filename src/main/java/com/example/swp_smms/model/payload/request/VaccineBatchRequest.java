package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class VaccineBatchRequest {
    private String stockInDate;
    private String expiryDate;
    private int quantity; // NEW field
}
