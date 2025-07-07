package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MedicationSentResponse {
    private Long medSentId;
    private UUID studentId;
    private UUID parentId;
    private String requestDate;     // e.g., "2025-07-08"
    private String sentAt;          // submission timestamp (today)
    private List<DosageResponse> dosages;
}
