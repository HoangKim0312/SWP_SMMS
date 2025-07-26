package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.VaccinationConfirmationRequest;
import com.example.swp_smms.model.payload.request.VaccinationConfirmationStatusRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.NoticeStatisticalResponse;
import com.example.swp_smms.model.payload.response.VaccinationConfirmationResponse;

import java.util.List;
import java.util.UUID;

public interface VaccinationConfirmationService {
    VaccinationConfirmationResponse getConfirmationById(Long id);
    List<VaccinationConfirmationResponse> getAllConfirmations();
    List<VaccinationConfirmationResponse> getConfirmationsByStudent(UUID studentId);
    List<VaccinationConfirmationResponse> getConfirmationsByParent(UUID parentId);
    List<VaccinationConfirmationResponse> getConfirmationsByNotice(Long vaccineNoticeId);
    List<VaccinationConfirmationResponse> getConfirmationsByStatus(String status);
    VaccinationConfirmationResponse updateConfirmation(VaccinationConfirmationRequest request);
    void deleteConfirmation(Long id);
    List<AccountResponse> getConfirmedStudentsByNotice(Long vaccineNoticeId);
    List<VaccinationConfirmationResponse> getConfirmationsByStatusAndParentId(String status, UUID parentId);
    List<VaccinationConfirmationResponse> confirmAllUpcomingNoticesForAllChildren(UUID parentId);
    int confirmAllPendingByParent(UUID parentId);
    VaccinationConfirmationResponse updateStatusOnly(VaccinationConfirmationStatusRequest request);
    NoticeStatisticalResponse getStatusCountsByNoticeId(Long noticeId);

} 