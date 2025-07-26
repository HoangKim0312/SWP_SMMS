package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class HealthEventRequest
{
    private String eventDate;
    private String eventType;
    private String description;
    private String solution;
    private String note;
    private String status;
    private List<HealthEventMedicationRequest> medications;
}
