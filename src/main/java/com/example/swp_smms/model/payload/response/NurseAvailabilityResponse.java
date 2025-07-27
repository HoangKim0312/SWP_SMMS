package com.example.swp_smms.model.payload.response;

import com.example.swp_smms.model.enums.ConsultationSlot;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class NurseAvailabilityResponse {
    private UUID nurseId;
    private String nurseName;
    private String nurseEmail;
    private List<ConsultationSlot> availableSlots;
    private List<ConsultationSlot> bookedSlots;
} 