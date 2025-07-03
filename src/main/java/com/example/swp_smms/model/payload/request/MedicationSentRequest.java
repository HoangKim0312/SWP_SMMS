package com.example.swp_smms.model.payload.request;

import lombok.Data;
import java.util.List;

@Data
public class MedicationSentRequest {
    private String requestDate; // Only today or tomorrow (expected in format "yyyy-MM-dd")
    private List<DosageRequest> dosages;
}
