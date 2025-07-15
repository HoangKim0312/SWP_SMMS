package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.HealthCheckRecordRequest;
import com.example.swp_smms.model.payload.response.HealthCheckRecordResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HealthCheckRecordService {
    HealthCheckRecordResponse createRecord(HealthCheckRecordRequest request, UUID studentId, UUID nurseId);
    HealthCheckRecordResponse getRecordById(Long recordId);
    List<HealthCheckRecordResponse> getAllRecords();
    List<HealthCheckRecordResponse> getRecordsByStudent(UUID studentId);
    List<HealthCheckRecordResponse> getRecordsByNurse(UUID nurseId);
    List<HealthCheckRecordResponse> getRecordsByNotice(Long checkNoticeId);
    List<HealthCheckRecordResponse> getRecordsByDate(LocalDate date);

    HealthCheckRecordResponse updateRecord(Long recordId, HealthCheckRecordRequest request);
    void deleteRecord(Long recordId);
} 