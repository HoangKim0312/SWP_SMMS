package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.HealthEventRequest;
import com.example.swp_smms.model.payload.response.HealthEventResponse;

import java.util.List;
import java.util.UUID;

public interface HealthEventService {
    HealthEventResponse createHealthEvent(UUID studentID, UUID nurseID, HealthEventRequest healthEventRequest);
    List<HealthEventResponse> viewAllHealthEvents();

}
