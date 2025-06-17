package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.EventMedicationUsageRequest;
import com.example.swp_smms.model.payload.response.EventMedicationUsageResponse;

import java.util.List;

public interface EventMedicationUsageService {
    EventMedicationUsageResponse createUsage(EventMedicationUsageRequest request);
    EventMedicationUsageResponse getUsageById(Long id);
    List<EventMedicationUsageResponse> getAllUsages();
    List<EventMedicationUsageResponse> getUsagesByHealthEvent(Long eventId);
    List<EventMedicationUsageResponse> getUsagesByMedication(Long medicationId);
    EventMedicationUsageResponse updateUsage(Long id, EventMedicationUsageRequest request);
    void deleteUsage(Long id);
} 