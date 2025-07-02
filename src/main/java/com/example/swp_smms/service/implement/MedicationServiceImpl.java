package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Medication;
import com.example.swp_smms.model.payload.request.MedicationRequest;
import com.example.swp_smms.model.payload.response.MedicationResponse;
import com.example.swp_smms.repository.MedicationRepository;
import com.example.swp_smms.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    @Override
    public MedicationResponse createMedication(MedicationRequest request) {
        Medication medication = new Medication();
        medication.setName(request.getName());
        medication.setDescription(request.getDescription());
        medication.setQuantity(request.getQuantity());
        medication.setQuantityType(request.getQuantityType());
        medication.setMedicationType(request.getMedicationType());
        medication.setExpiryDate(request.getExpiryDate());

        Medication savedMedication = medicationRepository.save(medication);
        return mapToResponse(savedMedication);
    }

    @Override
    public MedicationResponse getMedicationById(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));
        return mapToResponse(medication);
    }

    @Override
    public List<MedicationResponse> getAllMedications() {
        return medicationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicationResponse> searchMedicationsByName(String name) {
        return medicationRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicationResponse> getLowStockMedications(Integer threshold) {
        return medicationRepository.findByQuantityLessThan(threshold).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicationResponse updateMedication(Long id, MedicationRequest request) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));

        medication.setName(request.getName());
        medication.setDescription(request.getDescription());
        medication.setQuantity(request.getQuantity());
        medication.setQuantityType(request.getQuantityType());
        medication.setMedicationType(request.getMedicationType());
        medication.setExpiryDate(request.getExpiryDate());

        Medication updatedMedication = medicationRepository.save(medication);
        return mapToResponse(updatedMedication);
    }

    @Override
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new RuntimeException("Medication not found with id: " + id);
        }
        medicationRepository.deleteById(id);
    }

    private MedicationResponse mapToResponse(Medication medication) {
        return MedicationResponse.builder()
                .medicationId(medication.getMedicationId())
                .name(medication.getName())
                .description(medication.getDescription())
                .quantity(medication.getQuantity())
                .quantityType(medication.getQuantityType())
                .medicationType(medication.getMedicationType())
                .expiryDate(medication.getExpiryDate())
                .build();
    }
} 