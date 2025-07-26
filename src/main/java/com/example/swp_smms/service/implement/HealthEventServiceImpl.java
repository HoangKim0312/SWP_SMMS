package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.entity.HealthEventMedication;
import com.example.swp_smms.model.entity.Medication;
import com.example.swp_smms.model.payload.request.HealthEventRequest;
import com.example.swp_smms.model.payload.request.HealthEventMedicationRequest;
import com.example.swp_smms.model.payload.response.HealthEventResponse;
import com.example.swp_smms.model.payload.response.HealthEventMedicationResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.HealthEventRepository;
import com.example.swp_smms.repository.HealthEventMedicationRepository;
import com.example.swp_smms.repository.MedicationRepository;
import com.example.swp_smms.service.HealthEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HealthEventServiceImpl implements HealthEventService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private HealthEventRepository healthEventRepository;
    
    @Autowired
    private HealthEventMedicationRepository healthEventMedicationRepository;
    
    @Autowired
    private MedicationRepository medicationRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HealthEventResponse createHealthEvent(UUID studentId, UUID nurseId, HealthEventRequest request) {
        // Validate student exists
        if (!accountRepository.existsByAccountIdAndRole_RoleId(studentId, 1L)) {
            throw new RuntimeException("Student not exists or not a student");
        }
        
        // Validate nurse exists
        if (!accountRepository.existsByAccountIdAndRole_RoleId(nurseId, 3L)) {
            throw new RuntimeException("Nurse not exists or not a nurse");
        }
        
        // Map request to entity
        HealthEvent event = modelMapper.map(request, HealthEvent.class);
        
        // Set student and nurse entities
        Account student = accountRepository.findAccountByAccountId(studentId);
        Account nurse = accountRepository.findAccountByAccountId(nurseId);
        event.setStudent(student);
        event.setNurse(nurse);
        
        // Save health event first
        HealthEvent savedEvent = healthEventRepository.save(event);
        
        // Handle medications if provided
        if (request.getMedications() != null && !request.getMedications().isEmpty()) {
            for (HealthEventMedicationRequest medRequest : request.getMedications()) {
                // Validate medication exists
                Medication medication = medicationRepository.findById(medRequest.getMedicationId())
                        .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medRequest.getMedicationId()));
                
                // Create health event medication
                HealthEventMedication healthEventMedication = new HealthEventMedication();
                healthEventMedication.setHealthEvent(savedEvent);
                healthEventMedication.setMedication(medication);
                healthEventMedication.setDosageAmount(medRequest.getDosageAmount());
                healthEventMedication.setDosageUnit(medRequest.getDosageUnit());
                healthEventMedication.setFrequency(medRequest.getFrequency());
                healthEventMedication.setDuration(medRequest.getDuration());
                healthEventMedication.setAdministrationNotes(medRequest.getAdministrationNotes());
                healthEventMedication.setUsageDate(medRequest.getUsageDate());
                healthEventMedication.setUsageTime(medRequest.getUsageTime());
                
                healthEventMedicationRepository.save(healthEventMedication);
            }
        }
        
        // Map to response
        HealthEventResponse response = modelMapper.map(savedEvent, HealthEventResponse.class);
        response.setEventId(savedEvent.getEventId());
        response.setStudentID(studentId);
        response.setNurseID(nurseId);
        
        // Add medications to response
        response.setMedications(getMedicationsByHealthEvent(savedEvent.getEventId()));
        
        return response;
    }

    @Override
    public List<HealthEventResponse> viewAllHealthEvents() {
        List<HealthEvent> events = healthEventRepository.findAll();
        return events.stream()
                .map(event -> {
                    HealthEventResponse response = modelMapper.map(event, HealthEventResponse.class);
                    response.setEventId(event.getEventId());
                    response.setStudentID(event.getStudent().getAccountId());
                    if (event.getNurse() != null) {
                        response.setNurseID(event.getNurse().getAccountId());
                    }
                    response.setMedications(getMedicationsByHealthEvent(event.getEventId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthEventResponse> viewHealthEventsByDate(String eventDate) {
        List<HealthEvent> events = healthEventRepository.findByEventDate(eventDate);
        return events.stream()
                .map(event -> {
                    HealthEventResponse response = modelMapper.map(event, HealthEventResponse.class);
                    response.setEventId(event.getEventId());
                    response.setStudentID(event.getStudent().getAccountId());
                    if (event.getNurse() != null) {
                        response.setNurseID(event.getNurse().getAccountId());
                    }
                    response.setMedications(getMedicationsByHealthEvent(event.getEventId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public HealthEventResponse updateHealthEvent(Long eventId, HealthEventRequest request) {
        HealthEvent event = healthEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("HealthEvent not found with id: " + eventId));

        modelMapper.map(request, event);

        HealthEvent updatedEvent = healthEventRepository.save(event);
        
        // Handle medications update
        if (request.getMedications() != null) {
            // Delete existing medications for this health event
            healthEventMedicationRepository.deleteByHealthEvent_EventId(eventId);
            
            // Add new medications
            for (HealthEventMedicationRequest medRequest : request.getMedications()) {
                // Validate medication exists
                Medication medication = medicationRepository.findById(medRequest.getMedicationId())
                        .orElseThrow(() -> new RuntimeException("Medication not found with id: " + medRequest.getMedicationId()));
                
                // Create health event medication
                HealthEventMedication healthEventMedication = new HealthEventMedication();
                healthEventMedication.setHealthEvent(updatedEvent);
                healthEventMedication.setMedication(medication);
                healthEventMedication.setDosageAmount(medRequest.getDosageAmount());
                healthEventMedication.setDosageUnit(medRequest.getDosageUnit());
                healthEventMedication.setFrequency(medRequest.getFrequency());
                healthEventMedication.setDuration(medRequest.getDuration());
                healthEventMedication.setAdministrationNotes(medRequest.getAdministrationNotes());
                healthEventMedication.setUsageDate(medRequest.getUsageDate());
                healthEventMedication.setUsageTime(medRequest.getUsageTime());
                
                healthEventMedicationRepository.save(healthEventMedication);
            }
        }

        HealthEventResponse response = modelMapper.map(updatedEvent, HealthEventResponse.class);
        response.setEventId(updatedEvent.getEventId());
        response.setStudentID(updatedEvent.getStudent().getAccountId());
        if (updatedEvent.getNurse() != null) {
            response.setNurseID(updatedEvent.getNurse().getAccountId());
        }
        response.setMedications(getMedicationsByHealthEvent(updatedEvent.getEventId()));

        return response;
    }

    @Override
    public void deleteHealthEvent(Long eventId) {
        if (!healthEventRepository.existsById(eventId)) {
            throw new RuntimeException("Health Event not found with id: " + eventId);
        }
        healthEventRepository.deleteById(eventId);
    }
    
    @Override
    public List<HealthEventMedicationResponse> getMedicationsByHealthEvent(Long eventId) {
        List<HealthEventMedication> medications = healthEventMedicationRepository.findByHealthEvent_EventId(eventId);
        return medications.stream()
                .map(med -> {
                    HealthEventMedicationResponse response = new HealthEventMedicationResponse();
                    response.setHealthEventMedicationId(med.getHealthEventMedicationId());
                    response.setMedicationId(med.getMedication().getMedicationId());
                    response.setMedicationName(med.getMedication().getName());
                    response.setMedicationDescription(med.getMedication().getDescription());
                    response.setDosageAmount(med.getDosageAmount());
                    response.setDosageUnit(med.getDosageUnit());
                    response.setFrequency(med.getFrequency());
                    response.setDuration(med.getDuration());
                    response.setAdministrationNotes(med.getAdministrationNotes());
                    response.setUsageDate(med.getUsageDate());
                    response.setUsageTime(med.getUsageTime());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
