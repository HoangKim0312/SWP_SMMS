package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.VaccinationRecordRequest;
import com.example.swp_smms.model.payload.response.VaccinationRecordResponse;

import java.util.List;
import java.util.UUID;

public interface VaccinationRecordService {
    VaccinationRecordResponse createRecord(VaccinationRecordRequest request, UUID nurseId);
    VaccinationRecordResponse getRecordById(Long id);
    List<VaccinationRecordResponse> getAllRecords();
    List<VaccinationRecordResponse> getRecordsByStudent(UUID studentId);
    List<VaccinationRecordResponse> getRecordsByNurse(UUID nurseId);
    List<VaccinationRecordResponse> getRecordsByNotice(Long vaccineNoticeId);
    VaccinationRecordResponse updateRecord(Long id, VaccinationRecordRequest request);
    void deleteRecord(Long id);
} 