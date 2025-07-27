package com.example.swp_smms.controller;

import com.example.swp_smms.model.enums.ConsultationSlot;
import com.example.swp_smms.model.payload.request.ConsultationScheduleRequest;
import com.example.swp_smms.model.payload.response.ConsultationScheduleResponse;
import com.example.swp_smms.model.payload.response.NurseAvailabilityResponse;
import com.example.swp_smms.service.ConsultationScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
public class ConsultationScheduleController {
    private final ConsultationScheduleService consultationScheduleService;

    @PostMapping("/schedule")
    public Object scheduleConsultation(@RequestBody ConsultationScheduleRequest request) {
        ConsultationScheduleResponse response = consultationScheduleService.scheduleConsultation(request);
        return response;
    }

    @GetMapping("/search")
    public List<ConsultationScheduleResponse> searchConsultationsByDate(
            @RequestParam LocalDate date,
            @RequestParam(defaultValue = "asc") String sort) {
        return consultationScheduleService.searchConsultationsByDate(date, sort);
    }

    @GetMapping("/nurse-availability")
    public List<ConsultationSlot> getAvailableSlotsForNurse(
            @RequestParam UUID nurseId,
            @RequestParam LocalDate date) {
        return consultationScheduleService.getAvailableSlotsForNurse(nurseId, date);
    }
    
    // New endpoints for parent-driven booking
    @GetMapping("/all-nurse-availability")
    public List<NurseAvailabilityResponse> getAllNurseAvailability(
            @RequestParam LocalDate date) {
        return consultationScheduleService.getAllNurseAvailability(date);
    }
    
    @GetMapping("/parent-consultations")
    public List<ConsultationScheduleResponse> getParentConsultations(
            @RequestParam UUID parentId) {
        return consultationScheduleService.getParentConsultations(parentId);
    }
    
    @GetMapping("/student-consultations")
    public List<ConsultationScheduleResponse> getStudentConsultations(
            @RequestParam UUID studentId) {
        return consultationScheduleService.getStudentConsultations(studentId);
    }
    
    @PutMapping("/cancel/{consultationId}")
    public ConsultationScheduleResponse cancelConsultation(
            @PathVariable Long consultationId,
            @RequestParam UUID parentId) {
        return consultationScheduleService.cancelConsultation(consultationId, parentId);
    }
} 