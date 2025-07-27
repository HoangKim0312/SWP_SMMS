package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.ConsultationScheduleRequest;
import com.example.swp_smms.model.payload.response.ConsultationScheduleResponse;
import com.example.swp_smms.model.enums.ConsultationSlot;
import com.example.swp_smms.model.payload.response.NurseAvailabilityResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ConsultationScheduleService {
    ConsultationScheduleResponse scheduleConsultation(ConsultationScheduleRequest request);
    List<ConsultationScheduleResponse> searchConsultationsByDate(LocalDate date, String sort);
    List<ConsultationSlot> getAvailableSlotsForNurse(UUID nurseId, LocalDate date);
    
    // New methods for parent-driven booking
    List<NurseAvailabilityResponse> getAllNurseAvailability(LocalDate date);
    List<ConsultationScheduleResponse> getParentConsultations(UUID parentId);
    List<ConsultationScheduleResponse> getStudentConsultations(UUID studentId);
    ConsultationScheduleResponse cancelConsultation(Long consultationId, UUID parentId);
} 