package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.enums.ConsultationSlot;
import com.example.swp_smms.model.payload.request.ConsultationScheduleRequest;
import com.example.swp_smms.model.payload.response.ConsultationScheduleResponse;
import com.example.swp_smms.model.payload.response.NurseAvailabilityResponse;
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
        // Validate nurse, student, parent, healthCheckRecord
        Account nurse = accountRepository.findById(request.getNurseId())
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        HealthCheckRecord record = healthCheckRecordRepository.findById(request.getHealthCheckRecordId())
                .orElseThrow(() -> new RuntimeException("HealthCheckRecord not found"));

        // Validate slot
        List<ConsultationSchedule> existing = consultationScheduleRepository.findByNurse_AccountIdAndScheduledDateAndSlot(
                request.getNurseId(), request.getScheduledDate(), request.getSlot());
        if (!existing.isEmpty()) {
            throw new RuntimeException("This nurse is already booked for this slot on this date.");
        }

        // Create and save
        ConsultationSchedule schedule = new ConsultationSchedule();
        schedule.setStudent(student);
        schedule.setParent(parent);
        schedule.setNurse(nurse);
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
    public List<ConsultationSlot> getAvailableSlotsForNurse(UUID nurseId, LocalDate date) {
        List<ConsultationSchedule> booked = consultationScheduleRepository.findByNurse_AccountIdAndScheduledDate(nurseId, date);
        Set<ConsultationSlot> bookedSlots = booked.stream().map(ConsultationSchedule::getSlot).collect(Collectors.toSet());
        List<ConsultationSlot> allSlots = Arrays.asList(ConsultationSlot.values());
        return allSlots.stream().filter(slot -> !bookedSlots.contains(slot)).collect(Collectors.toList());
    }
    
    @Override
    public List<NurseAvailabilityResponse> getAllNurseAvailability(LocalDate date) {
        // Get all nurses (roleId = 3 for Nurse role)
        List<Account> nurses = accountRepository.findByRole_RoleId(3L, org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
        
        List<NurseAvailabilityResponse> responses = new ArrayList<>();
        for (Account nurse : nurses) {
            NurseAvailabilityResponse response = new NurseAvailabilityResponse();
            response.setNurseId(nurse.getAccountId());
            response.setNurseName(nurse.getFullName());
            response.setNurseEmail(nurse.getEmail());
            
            // Get available and booked slots for this nurse
            List<ConsultationSlot> availableSlots = getAvailableSlotsForNurse(nurse.getAccountId(), date);
            List<ConsultationSchedule> bookedSchedules = consultationScheduleRepository.findByNurse_AccountIdAndScheduledDate(nurse.getAccountId(), date);
            List<ConsultationSlot> bookedSlots = bookedSchedules.stream().map(ConsultationSchedule::getSlot).collect(Collectors.toList());
            
            response.setAvailableSlots(availableSlots);
            response.setBookedSlots(bookedSlots);
            responses.add(response);
        }
        
        return responses;
    }
    
    @Override
    public List<ConsultationScheduleResponse> getParentConsultations(UUID parentId) {
        List<ConsultationSchedule> consultations = consultationScheduleRepository.findByParent_AccountId(parentId);
        return consultations.stream()
                .map(schedule -> modelMapper.map(schedule, ConsultationScheduleResponse.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ConsultationScheduleResponse> getStudentConsultations(UUID studentId) {
        List<ConsultationSchedule> consultations = consultationScheduleRepository.findByStudent_AccountId(studentId);
        return consultations.stream()
                .map(schedule -> modelMapper.map(schedule, ConsultationScheduleResponse.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public ConsultationScheduleResponse cancelConsultation(Long consultationId, UUID parentId) {
        ConsultationSchedule consultation = consultationScheduleRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        
        // Verify parent owns this consultation
        if (!consultation.getParent().getAccountId().equals(parentId)) {
            throw new RuntimeException("Parent not authorized to cancel this consultation");
        }
        
        consultation.setStatus("CANCELLED");
        ConsultationSchedule saved = consultationScheduleRepository.save(consultation);
        return modelMapper.map(saved, ConsultationScheduleResponse.class);
    }
} 