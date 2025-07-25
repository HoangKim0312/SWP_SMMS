package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.HealthCheckNoticeRequest;
import com.example.swp_smms.model.payload.response.HealthCheckNoticeResponse;

import java.time.LocalDate;
import java.util.List;

public interface HealthCheckNoticeService {
    HealthCheckNoticeResponse createNotice(HealthCheckNoticeRequest request);
    HealthCheckNoticeResponse getNoticeById(Long checkNoticeId);
    List<HealthCheckNoticeResponse> getAllNotices();
    List<HealthCheckNoticeResponse> getNoticesByDate(LocalDate date);
    List<HealthCheckNoticeResponse> getNoticesByTitle(String title);
    List<HealthCheckNoticeResponse> getNoticesByGrade(Integer grade);
    List<HealthCheckNoticeResponse> getNoticesByPriority(String priority);
    HealthCheckNoticeResponse updateNotice(Long checkNoticeId, HealthCheckNoticeRequest request);
    void deleteNotice(Long checkNoticeId);
} 