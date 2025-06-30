package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.HealthEventFollowUpRequest;
import com.example.swp_smms.model.payload.response.HealthEventFollowUpResponse;

import java.util.List;
import java.util.UUID;

public interface HealthEventFollowUpService {
    HealthEventFollowUpResponse createFollowUp(HealthEventFollowUpRequest request);
    List<HealthEventFollowUpResponse> getFollowUpsByEvent(Long eventId);
    List<HealthEventFollowUpResponse> getFollowUpsByParent(UUID parentId);
    HealthEventFollowUpResponse updateFollowUpStatus(Long followUpId, String status);
    HealthEventFollowUpResponse acknowledgeFollowUp(Long followUpId);
} 