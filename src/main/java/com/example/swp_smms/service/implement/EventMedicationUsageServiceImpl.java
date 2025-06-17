package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.EventMedicationUsage;
import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.entity.Medication;
import com.example.swp_smms.model.payload.request.EventMedicationUsageRequest;
import com.example.swp_smms.model.payload.response.EventMedicationUsageResponse;
import com.example.swp_smms.repository.EventMedicationUsageRepository;
import com.example.swp_smms.repository.HealthEventRepository;
import com.example.swp_smms.repository.MedicationRepository;
import com.example.swp_smms.service.EventMedicationUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventMedicationUsageServiceImpl implements EventMedicationUsageService {

    private final EventMedicationUsageRepository usageRepository;
    private final HealthEventRepository healthEventRepository;
    private final MedicationRepository medicationRepository;

    @Override
    @Transactional
    public EventMedicationUsageResponse createUsage(EventMedicationUsageRequest request) {
        HealthEvent healthEvent = healthEventRepository.findById(request.getHealthEventId())
                .orElseThrow(() -> new RuntimeException("Health event not found with id: " + request.getHealthEventId()));

        Medication medication = medicationRepository.findById(request.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + request.getMedicationId()));

        // Check if there's enough stock
        if (medication.getQuantity() < request.getQuantityUsed()) {
            throw new RuntimeException("Insufficient medication stock. Available: " + medication.getQuantity() + ", Required: " + request.getQuantityUsed());
        }

        EventMedicationUsage usage = new EventMedicationUsage();
        usage.setHealthEvent(healthEvent);
        usage.setMedication(medication);
        usage.setQuantityUsed(request.getQuantityUsed());
        usage.setTime(request.getTime());
        usage.setDosage(request.getDosage());

        // Update medication stock
        medication.setQuantity(medication.getQuantity() - request.getQuantityUsed());
        medicationRepository.save(medication);

        EventMedicationUsage savedUsage = usageRepository.save(usage);
        return mapToResponse(savedUsage);
    }

    @Override
    public EventMedicationUsageResponse getUsageById(Long id) {
        EventMedicationUsage usage = usageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usage not found with id: " + id));
        return mapToResponse(usage);
    }

    @Override
    public List<EventMedicationUsageResponse> getAllUsages() {
        return usageRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventMedicationUsageResponse> getUsagesByHealthEvent(Long eventId) {
        return usageRepository.findByHealthEvent_EventId(eventId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventMedicationUsageResponse> getUsagesByMedication(Long medicationId) {
        return usageRepository.findByMedication_MedicationId(medicationId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventMedicationUsageResponse updateUsage(Long id, EventMedicationUsageRequest request) {
        EventMedicationUsage usage = usageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usage not found with id: " + id));

        HealthEvent healthEvent = healthEventRepository.findById(request.getHealthEventId())
                .orElseThrow(() -> new RuntimeException("Health event not found with id: " + request.getHealthEventId()));

        Medication medication = medicationRepository.findById(request.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + request.getMedicationId()));

        // Restore previous quantity and check new quantity
        Medication oldMedication = usage.getMedication();
        oldMedication.setQuantity(oldMedication.getQuantity() + usage.getQuantityUsed());
        medicationRepository.save(oldMedication);

        if (medication.getQuantity() < request.getQuantityUsed()) {
            throw new RuntimeException("Insufficient medication stock. Available: " + medication.getQuantity() + ", Required: " + request.getQuantityUsed());
        }

        usage.setHealthEvent(healthEvent);
        usage.setMedication(medication);
        usage.setQuantityUsed(request.getQuantityUsed());
        usage.setTime(request.getTime());
        usage.setDosage(request.getDosage());

        // Update new medication stock
        medication.setQuantity(medication.getQuantity() - request.getQuantityUsed());
        medicationRepository.save(medication);

        EventMedicationUsage updatedUsage = usageRepository.save(usage);
        return mapToResponse(updatedUsage);
    }

    @Override
    @Transactional
    public void deleteUsage(Long id) {
        EventMedicationUsage usage = usageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usage not found with id: " + id));

        // Restore medication stock
        Medication medication = usage.getMedication();
        medication.setQuantity(medication.getQuantity() + usage.getQuantityUsed());
        medicationRepository.save(medication);

        usageRepository.deleteById(id);
    }

    private EventMedicationUsageResponse mapToResponse(EventMedicationUsage usage) {
        return EventMedicationUsageResponse.builder()
                .usageId(usage.getUsageId())
                .healthEventId(usage.getHealthEvent().getEventId())
                .healthEventDescription(usage.getHealthEvent().getDescription())
                .medicationId(usage.getMedication().getMedicationId())
                .medicationName(usage.getMedication().getName())
                .quantityUsed(usage.getQuantityUsed())
                .time(usage.getTime())
                .dosage(usage.getDosage())
                .build();
    }
} 