package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.enums.ConsultationSlot;
import com.example.swp_smms.model.payload.request.ConsultationScheduleRequest;
import com.example.swp_smms.model.payload.response.ConsultationScheduleResponse;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.ConsultationScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationScheduleServiceImpl implements ConsultationScheduleService {
    private final ConsultationScheduleRepository consultationScheduleRepository;
    private final AccountRepository accountRepository;
    private final HealthCheckRecordRepository healthCheckRecordRepository;
    private final ModelMapper modelMapper;

    @Override
    public ConsultationScheduleResponse scheduleConsultation(ConsultationScheduleRequest request) {
        // Validate staff, student, parent, healthCheckRecord
        Account staff = accountRepository.findById(request.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        HealthCheckRecord record = healthCheckRecordRepository.findById(request.getHealthCheckRecordId())
                .orElseThrow(() -> new RuntimeException("HealthCheckRecord not found"));

        // Validate slot
        List<ConsultationSchedule> existing = consultationScheduleRepository.findByStaff_AccountIdAndScheduledDateAndSlot(
                request.getStaffId(), request.getScheduledDate(), request.getSlot());
        if (!existing.isEmpty()) {
            throw new RuntimeException("This staff is already booked for this slot on this date.");
        }

        // Create and save
        ConsultationSchedule schedule = new ConsultationSchedule();
        schedule.setStudent(student);
        schedule.setParent(parent);
        schedule.setStaff(staff);
        schedule.setHealthCheckRecord(record);
        schedule.setScheduledDate(request.getScheduledDate());
        schedule.setSlot(request.getSlot());
        schedule.setStatus("SCHEDULED");
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setReason(request.getReason());
        ConsultationSchedule saved = consultationScheduleRepository.save(schedule);
        return modelMapper.map(saved, ConsultationScheduleResponse.class);
    }

    @Override
    public List<ConsultationScheduleResponse> searchConsultationsByDate(LocalDate date, String sort) {
        List<ConsultationSchedule> list;
        if ("desc".equalsIgnoreCase(sort)) {
            list = consultationScheduleRepository.findByScheduledDateOrderByScheduledDateDesc(date);
        } else {
            list = consultationScheduleRepository.findByScheduledDateOrderByScheduledDateAsc(date);
        }
        return list.stream().map(s -> modelMapper.map(s, ConsultationScheduleResponse.class)).collect(Collectors.toList());
    }

    @Override
    public List<ConsultationSlot> getAvailableSlotsForStaff(UUID staffId, LocalDate date) {
        List<ConsultationSchedule> booked = consultationScheduleRepository.findByStaff_AccountIdAndScheduledDate(staffId, date);
        Set<ConsultationSlot> bookedSlots = booked.stream().map(ConsultationSchedule::getSlot).collect(Collectors.toSet());
        List<ConsultationSlot> allSlots = Arrays.asList(ConsultationSlot.values());
        return allSlots.stream().filter(slot -> !bookedSlots.contains(slot)).collect(Collectors.toList());
    }
} 