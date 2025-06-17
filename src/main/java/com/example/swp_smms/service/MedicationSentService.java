package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.MedicationSentResponse;

import java.util.List;
import java.util.UUID;

public interface MedicationSentService {

    MedicationSentResponse createMedicationSent(UUID parentId, UUID studentId, String sentAt,MedicationSentRequest request);

    List<MedicationSentResponse> getAllMedicationSentsForStudent(UUID studentId);
}
