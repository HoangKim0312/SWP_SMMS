package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class HealthEventResponse {
    private Long eventId;
    private String eventDate;
    private String eventType;
    private String description;
    private String solution;
    private String note;
    private String status;
    private UUID studentID;
    private UUID nurseID;
}
