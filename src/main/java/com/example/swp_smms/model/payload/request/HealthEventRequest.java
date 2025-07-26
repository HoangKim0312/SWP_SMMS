package com.example.swp_smms.model.payload.request;

import com.example.swp_smms.model.enums.HealthEventPriority;
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
    private HealthEventPriority priority;
    private Boolean requiresHomeCare;
    private List<HealthEventMedicationRequest> medications;
    private HealthEventFollowUpRequest followUp; // Optional follow-up to create with the event
}
