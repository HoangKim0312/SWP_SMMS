package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MedicationSentRequest {
    private LocalDate requestDate; // Only today or tomorrow (expected in format "yyyy-MM-dd")
    private List<DosageRequest> dosages;
}
