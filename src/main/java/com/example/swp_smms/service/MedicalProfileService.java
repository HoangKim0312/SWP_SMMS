package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.ListMedicalProfileResponse;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;

import java.util.UUID;

public interface MedicalProfileService {
    MedicalProfileResponse createMedicalProfile(UUID studentId,Long recordID, MedicalProfileRequest request);

    MedicalProfileResponse getLastMedicalProfile(UUID studentId);

    ListMedicalProfileResponse getAllMedicalProfiles(UUID studentId);

    void deleteMedicalProfile(UUID studentId, Long medicalProfileId);

}
