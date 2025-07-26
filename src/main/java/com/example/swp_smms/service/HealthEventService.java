package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.HealthEventRequest;
import com.example.swp_smms.model.payload.request.HealthEventApprovalRequest;
import com.example.swp_smms.model.payload.response.HealthEventResponse;
import com.example.swp_smms.model.payload.response.HealthEventMedicationResponse;
import com.example.swp_smms.model.payload.response.HealthEventApprovalResponse;

import java.util.List;
import java.util.UUID;

public interface HealthEventService {
    HealthEventResponse createHealthEvent(UUID studentID, UUID nurseID, HealthEventRequest healthEventRequest);
    List<HealthEventResponse> viewAllHealthEvents();
    List<HealthEventResponse> viewHealthEventsByDate(String eventDate);
    HealthEventResponse updateHealthEvent(Long eventId, HealthEventRequest healthEventRequest);
    void deleteHealthEvent(Long eventId);
    List<HealthEventMedicationResponse> getMedicationsByHealthEvent(Long eventId);
    HealthEventApprovalResponse approveHealthEvent(HealthEventApprovalRequest request);
    List<HealthEventResponse> getHealthEventsByPriority(String priority);
    List<HealthEventResponse> getHealthEventsPendingApproval(UUID parentId);
}
