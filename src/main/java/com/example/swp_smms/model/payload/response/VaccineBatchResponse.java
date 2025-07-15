package com.example.swp_smms.model.payload.response;

import lombok.Data;

@Data
public class VaccineBatchResponse {
    private Long vaccineBatchId;
    private Long vaccineId;
    private String stockInDate;
    private String expiryDate;
    private int quantity;
}
