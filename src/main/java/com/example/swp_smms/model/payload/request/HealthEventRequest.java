package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class HealthEventRequest
{
    private LocalDate eventDate;
    private String eventType;
    private String description;
    private String solution;
    private String note;
    private String status;

}
