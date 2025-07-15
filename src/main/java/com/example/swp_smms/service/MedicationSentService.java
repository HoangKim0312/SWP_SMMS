package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.ListMedicationSentResponse;
import com.example.swp_smms.model.payload.response.MedicationSentResponse;

import java.time.LocalDate;
import java.util.UUID;

public interface MedicationSentService {

    MedicationSentResponse createMedicationSent(UUID parentId, UUID studentId,MedicationSentRequest request);

    ListMedicationSentResponse getAllActiveMedicationSentsForStudent(UUID studentId);

    ListMedicationSentResponse getAllMedicationSentsForStudent(UUID studentId);

    void deleteMedicationSent(UUID studentId, Long medicationSentId);

    MedicationSentResponse updateMedicationSent(UUID studentId, Long medicationSentId, MedicationSentRequest request);

    ListMedicationSentResponse getAllActiveMedicationSentsForAllStudents();

    MedicationSentResponse getMedicationSentById(UUID studentId, Long medicationSentId);

    void updateMedicationSentAcceptance(Long medSentId, Boolean isAccepted);

    ListMedicationSentResponse getAcceptedMedicationSents(UUID studentId, LocalDate date);

    ListMedicationSentResponse getDeclinedMedicationSents(UUID studentId, LocalDate requestDate);


}
