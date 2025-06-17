package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.MedicationRequest;
import com.example.swp_smms.model.payload.response.MedicationResponse;

import java.util.List;

public interface MedicationService {
    MedicationResponse createMedication(MedicationRequest request);
    MedicationResponse getMedicationById(Long id);
    List<MedicationResponse> getAllMedications();
    List<MedicationResponse> searchMedicationsByName(String name);
    List<MedicationResponse> getLowStockMedications(Integer threshold);
    MedicationResponse updateMedication(Long id, MedicationRequest request);
    void deleteMedication(Long id);
} 