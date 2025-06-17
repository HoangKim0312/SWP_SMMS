package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.util.UUID;

@Data
public class MedicationSentRequest {
    private String medicationName;
    private String instructions;
    private String startDate;
    private String endDate;
    private Integer frequencyPerDay;
    private String timingNotes;
    private Integer amount;
}
