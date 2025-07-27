package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.entity.HealthEventMedication;
import com.example.swp_smms.model.entity.Medication;
import com.example.swp_smms.model.entity.StudentParent;
import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import com.example.swp_smms.model.enums.HealthEventPriority;
import com.example.swp_smms.model.payload.request.HealthEventRequest;
import com.example.swp_smms.model.payload.request.HealthEventMedicationRequest;
import com.example.swp_smms.model.payload.request.HealthEventFollowUpRequest;
import com.example.swp_smms.model.payload.request.HealthEventApprovalRequest;
import com.example.swp_smms.model.payload.response.HealthEventResponse;
import com.example.swp_smms.model.payload.response.HealthEventMedicationResponse;
import com.example.swp_smms.model.payload.response.HealthEventApprovalResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.HealthEventRepository;
import com.example.swp_smms.repository.HealthEventMedicationRepository;
import com.example.swp_smms.repository.MedicationRepository;
import com.example.swp_smms.repository.StudentParentRepository;
import com.example.swp_smms.service.HealthEventService;
import com.example.swp_smms.service.HealthEventNotificationService;
import com.example.swp_smms.service.HealthEventFollowUpService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private StudentParentRepository studentParentRepository;
    
    @Autowired
    private HealthEventNotificationService notificationService;
    
    @Autowired
    private HealthEventFollowUpService followUpService;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public HealthEventResponse createHealthEvent(UUID studentId, UUID nurseId, HealthEventRequest request) {
        // Validate student exists
        if (!accountRepository.existsByAccountIdAndRole_RoleId(studentId, 1L)) {
            throw new RuntimeException("Student not exists or not a student");
        }
        
        // Validate nurse exists
        if (!accountRepository.existsByAccountIdAndRole_RoleId(nurseId, 3L)) {
            throw new RuntimeException("Nurse not exists or not a nurse");
        }
        
        // Create health event entity manually to avoid ModelMapper issues
        HealthEvent event = new HealthEvent();
        event.setEventDate(request.getEventDate());
        event.setEventType(request.getEventType());
        event.setDescription(request.getDescription());
        event.setSolution(request.getSolution());
        event.setNote(request.getNote());
        event.setStatus(request.getStatus());
        event.setPriority(request.getPriority());
        event.setRequiresHomeCare(request.getRequiresHomeCare());
        
        // Set student and nurse entities
        Account student = accountRepository.findAccountByAccountId(studentId);
        Account nurse = accountRepository.findAccountByAccountId(nurseId);
        event.setStudent(student);
        event.setNurse(nurse);
        
        // Set priority (default to MEDIUM if not provided)
        if (event.getPriority() == null) {
            event.setPriority(HealthEventPriority.MEDIUM);
        }
        
        // Set approval status based on priority
        if (event.getPriority().requiresParentApproval()) {
            event.setParentApprovalStatus(HealthEventApprovalStatus.PENDING);
        } else {
            event.setParentApprovalStatus(HealthEventApprovalStatus.NOT_REQUIRED);
        }
        
        // Set home care requirement
        if (event.getRequiresHomeCare() == null) {
            // Auto-determine based on priority and event type
            event.setRequiresHomeCare(determineHomeCareRequirement(event));
        }
        
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
        
        // Handle priority-based actions
        handlePriorityBasedActions(savedEvent);
        
        // Handle scenario-based follow-up creation
        if (request.getFollowUp() != null || shouldCreateFollowUp(savedEvent)) {
            createFollowUpForEvent(savedEvent, request.getFollowUp());
        }
        
        // Map to response
        HealthEventResponse response = mapToResponse(savedEvent);
        
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

    @Override
    public HealthEventApprovalResponse approveHealthEvent(HealthEventApprovalRequest request) {
        // Validate health event exists
        HealthEvent healthEvent = healthEventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Health event not found with id: " + request.getEventId()));
        
        // Validate parent exists
        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + request.getParentId()));
        
        // Validate parent is linked to the student
        boolean isParentLinked = studentParentRepository.existsByStudent_AccountIdAndParent_AccountId(
                healthEvent.getStudent().getAccountId(), request.getParentId());
        
        if (!isParentLinked) {
            throw new RuntimeException("Parent is not linked to this student");
        }
        
        // Update approval status
        healthEvent.setParentApprovalStatus(request.getApprovalStatus());
        healthEvent.setParentApprovalReason(request.getReason());
        healthEvent.setParentApprovalDate(LocalDateTime.now());
        healthEvent.setApprovedByParent(parent);
        
        // Save the updated health event
        healthEventRepository.save(healthEvent);
        
        // Send approval confirmation email
        notificationService.sendApprovalConfirmation(healthEvent, request.getParentId());
        
        // Create response
        HealthEventApprovalResponse response = new HealthEventApprovalResponse();
        response.setEventId(healthEvent.getEventId());
        response.setParentId(request.getParentId());
        response.setParentName(parent.getFullName());
        response.setApprovalStatus(request.getApprovalStatus());
        response.setReason(request.getReason());
        response.setApprovalDate(healthEvent.getParentApprovalDate());
        response.setStudentName(healthEvent.getStudent().getFullName());
        response.setEventDescription(healthEvent.getDescription());
        response.setMessage("Health event " + request.getApprovalStatus().getDisplayName().toLowerCase() + " successfully");
        
        return response;
    }

    @Override
    public List<HealthEventResponse> getHealthEventsByPriority(String priority) {
        try {
            HealthEventPriority priorityEnum = HealthEventPriority.fromDisplayName(priority);
            List<HealthEvent> events = healthEventRepository.findByPriority(priorityEnum);
            return events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid priority: " + priority);
        }
    }

    @Override
    public List<HealthEventResponse> getHealthEventsPendingApproval(UUID parentId) {
        // Get all students linked to this parent
        List<StudentParent> studentParents = studentParentRepository.findByParent_AccountId(parentId);
        List<UUID> studentIds = studentParents.stream()
                .map(sp -> sp.getStudent().getAccountId())
                .collect(Collectors.toList());
        
        if (studentIds.isEmpty()) {
            return List.of();
        }
        
        // Get health events for these students that are pending approval
        List<HealthEvent> pendingEvents = healthEventRepository.findByStudent_AccountIdInAndParentApprovalStatus(
                studentIds, HealthEventApprovalStatus.PENDING);
        
        return pendingEvents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthEventResponse> getHealthEventsByNurseId(UUID nurseId) {
        // Validate nurse exists
        if (!accountRepository.existsByAccountIdAndRole_RoleId(nurseId, 3L)) {
            throw new RuntimeException("Nurse not exists or not a nurse");
        }
        
        // Get all health events by nurse ID
        List<HealthEvent> events = healthEventRepository.findByNurse_AccountId(nurseId);
        
        return events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private boolean determineHomeCareRequirement(HealthEvent event) {
        // Logic to determine if home care is required based on priority and event type
        if (event.getPriority() == HealthEventPriority.CRITICAL || event.getPriority() == HealthEventPriority.HIGH) {
            return true;
        }
        
        // Check event type for specific scenarios
        String eventType = event.getEventType() != null ? event.getEventType().toLowerCase() : "";
        String description = event.getDescription() != null ? event.getDescription().toLowerCase() : "";
        
        // Keywords that suggest home care is needed
        String[] homeCareKeywords = {"fever", "vomiting", "diarrhea", "headache", "injury", "fall", "accident", "bleeding"};
        
        for (String keyword : homeCareKeywords) {
            if (eventType.contains(keyword) || description.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }

    private boolean shouldCreateFollowUp(HealthEvent event) {
        // Create follow-up if:
        // 1. Priority is MEDIUM or higher
        // 2. Requires home care
        // 3. Has medications prescribed
        return (event.getPriority().requiresParentApproval() || 
                Boolean.TRUE.equals(event.getRequiresHomeCare()) ||
                (event.getMedications() != null && !event.getMedications().isEmpty()));
    }

    private void createFollowUpForEvent(HealthEvent event, HealthEventFollowUpRequest followUpRequest) {
        // Get parent for the student
        List<StudentParent> studentParents = studentParentRepository.findByStudent_AccountId(event.getStudent().getAccountId());
        
        if (studentParents.isEmpty()) {
            throw new RuntimeException("No parent found for student: " + event.getStudent().getFullName());
        }
        
        // Use the first parent (you might want to implement logic to choose the primary parent)
        StudentParent studentParent = studentParents.get(0);
        
        // Create follow-up request if not provided
        if (followUpRequest == null) {
            followUpRequest = new HealthEventFollowUpRequest();
            followUpRequest.setEventId(event.getEventId());
            followUpRequest.setParentId(studentParent.getParent().getAccountId());
            followUpRequest.setInstruction(generateFollowUpInstruction(event));
            followUpRequest.setRequiresDoctor(determineDoctorRequirement(event));
            followUpRequest.setStatus("PENDING");
        }
        
        // Create the follow-up
        followUpService.createFollowUp(followUpRequest);
    }

    private String generateFollowUpInstruction(HealthEvent event) {
        StringBuilder instruction = new StringBuilder();
        
        switch (event.getPriority()) {
            case LOW:
                instruction.append("Minor incident handled at school. Monitor for any changes.");
                break;
            case MEDIUM:
                instruction.append("Please monitor your child's condition. Contact school if symptoms worsen.");
                if (event.getMedications() != null && !event.getMedications().isEmpty()) {
                    instruction.append(" Follow medication instructions provided by the nurse.");
                }
                break;
            case HIGH:
                instruction.append("URGENT: Please monitor your child closely. Consider consulting a doctor if symptoms persist.");
                if (event.getMedications() != null && !event.getMedications().isEmpty()) {
                    instruction.append(" Administer medications as prescribed by the nurse.");
                }
                break;
            case CRITICAL:
                instruction.append("EMERGENCY: Immediate medical attention may be required. Please consult a doctor immediately.");
                break;
        }
        
        if (event.getSolution() != null && !event.getSolution().isEmpty()) {
            instruction.append(" Immediate action taken: ").append(event.getSolution());
        }
        
        return instruction.toString();
    }

    private Boolean determineDoctorRequirement(HealthEvent event) {
        return event.getPriority() == HealthEventPriority.CRITICAL || 
               event.getPriority() == HealthEventPriority.HIGH;
    }

    private void handlePriorityBasedActions(HealthEvent event) {
        // Get parent for the student
        List<StudentParent> studentParents = studentParentRepository.findByStudent_AccountId(event.getStudent().getAccountId());
        
        if (studentParents.isEmpty()) {
            throw new RuntimeException("No parent found for student: " + event.getStudent().getFullName());
        }
        
        // Notify all parents
        for (StudentParent studentParent : studentParents) {
            UUID parentId = studentParent.getParent().getAccountId();
            
            // Send appropriate notification based on priority
            if (event.getPriority().isEmergency()) {
                notificationService.sendEmergencyNotification(event, parentId);
            } else if (notificationService.shouldSendNotification(event.getPriority())) {
                notificationService.notifyParent(event, parentId);
            }
        }
    }

    private HealthEventResponse mapToResponse(HealthEvent event) {
        HealthEventResponse response = modelMapper.map(event, HealthEventResponse.class);
        response.setEventId(event.getEventId());
        response.setStudentID(event.getStudent().getAccountId());
        response.setNurseID(event.getNurse().getAccountId());
        response.setApprovedByParentID(event.getApprovedByParent() != null ? event.getApprovedByParent().getAccountId() : null);
        response.setApprovedByParentName(event.getApprovedByParent() != null ? event.getApprovedByParent().getFullName() : null);
        
        // Add medications to response
        response.setMedications(getMedicationsByHealthEvent(event.getEventId()));
        
        // Add follow-ups to response
        response.setFollowUps(followUpService.getFollowUpsByEvent(event.getEventId()));
        
        return response;
    }
}
