package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.StudentAllergy;
import com.example.swp_smms.model.entity.StudentCondition;
import com.example.swp_smms.model.entity.StudentDisease;
import com.example.swp_smms.model.payload.request.*;
import com.example.swp_smms.model.payload.response.ListMedicalProfileResponse;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;

import java.util.UUID;

public interface MedicalProfileService {
    MedicalProfile createMedicalProfile(MedicalProfileRequest request);

    StudentAllergy addAllergyToStudentProfile(AddAllergyToMedicalProfileRequest request);
    StudentDisease addStudentDisease(AddStudentDiseaseRequest request);
    StudentCondition addStudentCondition(AddStudentConditionRequest request);
    MedicalProfileResponse getFullMedicalProfile(String studentId);
}
