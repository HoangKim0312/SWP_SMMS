package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.ConsultationScheduleRequest;
import com.example.swp_smms.model.payload.response.ConsultationScheduleResponse;
import com.example.swp_smms.model.enums.ConsultationSlot;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ConsultationScheduleService {
    ConsultationScheduleResponse scheduleConsultation(ConsultationScheduleRequest request);
    List<ConsultationScheduleResponse> searchConsultationsByDate(LocalDate date, String sort);
    List<ConsultationSlot> getAvailableSlotsForStaff(UUID staffId, LocalDate date);
} 