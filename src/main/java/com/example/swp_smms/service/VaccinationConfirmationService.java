package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.VaccinationConfirmationRequest;
import com.example.swp_smms.model.payload.response.VaccinationConfirmationResponse;

import java.util.List;
import java.util.UUID;

public interface VaccinationConfirmationService {
    VaccinationConfirmationResponse createConfirmation(VaccinationConfirmationRequest request);
    VaccinationConfirmationResponse getConfirmationById(Long id);
    List<VaccinationConfirmationResponse> getAllConfirmations();
    List<VaccinationConfirmationResponse> getConfirmationsByStudent(UUID studentId);
    List<VaccinationConfirmationResponse> getConfirmationsByParent(UUID parentId);
    List<VaccinationConfirmationResponse> getConfirmationsByNotice(Long vaccineNoticeId);
    List<VaccinationConfirmationResponse> getConfirmationsByStatus(String status);
    VaccinationConfirmationResponse updateConfirmation(Long id, VaccinationConfirmationRequest request);
    void deleteConfirmation(Long id);
} 