package com.example.swp_smms.model.payload.request;

import lombok.Data;
import java.util.List;

@Data
public class DosageRequest {
    private String timingNotes; // e.g., "Before lunch", "After dinner"
    private List<MedicationItemRequest> medicationItems;
}
