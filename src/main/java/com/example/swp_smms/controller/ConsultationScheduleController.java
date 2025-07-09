package com.example.swp_smms.controller;

import com.example.swp_smms.model.enums.ConsultationSlot;
import com.example.swp_smms.model.payload.request.ConsultationScheduleRequest;
import com.example.swp_smms.model.payload.response.ConsultationScheduleResponse;
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

    @GetMapping("/staff-availability")
    public List<ConsultationSlot> getAvailableSlotsForStaff(
            @RequestParam UUID staffId,
            @RequestParam LocalDate date) {
        return consultationScheduleService.getAvailableSlotsForStaff(staffId, date);
    }
} 