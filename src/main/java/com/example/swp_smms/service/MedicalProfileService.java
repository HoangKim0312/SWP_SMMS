package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.StudentAllergy;
import com.example.swp_smms.model.payload.request.AddStudentAllergyRequest;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.ListMedicalProfileResponse;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;

import java.util.UUID;

public interface MedicalProfileService {
    MedicalProfile createMedicalProfile(MedicalProfileRequest request);

    StudentAllergy addStudentAllergy(AddStudentAllergyRequest request);
}
