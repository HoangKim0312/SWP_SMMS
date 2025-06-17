package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.ListMedicationSentResponse;
import com.example.swp_smms.model.payload.response.MedicationSentResponse;

import java.util.UUID;

public interface MedicationSentService {

    MedicationSentResponse createMedicationSent(UUID parentId, UUID studentId,MedicationSentRequest request);

    ListMedicationSentResponse getAllActiveMedicationSentsForStudent(UUID studentId);

    ListMedicationSentResponse getAllMedicationSentsForStudent(UUID studentId);

    void deleteMedicationSent(UUID studentId, Long medicationSentId);

    MedicationSentResponse updateMedicationSent(UUID studentId, Long medicationSentId, MedicationSentRequest request);

}
