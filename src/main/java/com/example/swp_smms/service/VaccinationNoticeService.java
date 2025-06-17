package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;

import java.util.List;

public interface VaccinationNoticeService {
    VaccinationNoticeResponse createNotice(VaccinationNoticeRequest request);
    VaccinationNoticeResponse getNoticeById(Long id);
    List<VaccinationNoticeResponse> getAllNotices();
    List<VaccinationNoticeResponse> searchNoticesByVaccineName(String vaccineName);
    VaccinationNoticeResponse updateNotice(Long id, VaccinationNoticeRequest request);
    void deleteNotice(Long id);
} 