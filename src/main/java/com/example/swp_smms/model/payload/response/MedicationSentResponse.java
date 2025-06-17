package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.util.UUID;

@Data
public class MedicationSentResponse {
    private Long medSentId;
    private UUID studentId;
    private UUID parentId;
    private String medicationName;
    private String instructions;
    private String startDate;
    private String endDate;
    private Integer frequencyPerDay;
    private String timingNotes;
    private String sentAt;
    private Integer amount;
}
