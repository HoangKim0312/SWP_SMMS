package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.HealthCheckConfirmationRequest;
import com.example.swp_smms.model.payload.response.HealthCheckConfirmationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface HealthCheckConfirmationService {
    HealthCheckConfirmationResponse createConfirmation(HealthCheckConfirmationRequest request);
    HealthCheckConfirmationResponse getConfirmationById(Long id);
    List<HealthCheckConfirmationResponse> getAllConfirmations();
    List<HealthCheckConfirmationResponse> getConfirmationsByStudent(UUID studentId);
    List<HealthCheckConfirmationResponse> getConfirmationsByParent(UUID parentId);
    List<HealthCheckConfirmationResponse> getConfirmationsByNotice(Long checkNoticeId);
    List<HealthCheckConfirmationResponse> getConfirmationsByStatus(String status);
    List<HealthCheckConfirmationResponse> getConfirmationsByDate(LocalDateTime confirmedAt);
    List<HealthCheckConfirmationResponse> getConfirmationsByTitle(String title);
    HealthCheckConfirmationResponse updateConfirmation(Long id, HealthCheckConfirmationRequest request);
    void deleteConfirmation(Long id);
} 