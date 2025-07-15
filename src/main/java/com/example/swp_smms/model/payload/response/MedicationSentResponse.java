package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class MedicationSentResponse {
    private Long medSentId;
    private UUID studentId;
    private UUID parentId;
    private LocalDate requestDate;      // e.g., "2025-07-08"
    private LocalDate sentAt;           // submission date (today)
    private Boolean isAccepted;         // true, false, or null
    private List<DosageResponse> dosages;
}
