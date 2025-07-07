package com.example.swp_smms.model.payload.response;

import lombok.Data;
import java.util.List;

@Data
public class DosageResponse {
    private String timingNotes;                        // e.g., "Before lunch"
    private List<MedicationItemResponse> medicationItems;
}
