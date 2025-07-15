package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.NoticeStatisticalResponse;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VaccinationNoticeService {
    VaccinationNoticeResponse createNotice(VaccinationNoticeRequest request, Long vaccineBatchId);
    VaccinationNoticeResponse getNoticeById(Long id);
    List<VaccinationNoticeResponse> getAllNotices();
    List<VaccinationNoticeResponse> searchNoticesByVaccineName(String vaccineName);
    VaccinationNoticeResponse updateNotice(Long id, VaccinationNoticeRequest request);
    void deleteNotice(Long id);
    List<VaccinationNoticeResponse> getNoticesForToday();
    List<VaccinationNoticeResponse> getActiveNotices();
    List<VaccinationNoticeResponse> getActiveNoticesByParent(UUID parentId);
    List<VaccinationNoticeResponse> filterNotices(Long vaccineId, Long vaccineBatchId, LocalDate vaccinationDate, boolean exact);
}