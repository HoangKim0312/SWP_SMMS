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
    StudentAllergy updateAllergyActiveStatus(Long studentAllergyId, boolean active);
    StudentDisease updateDiseaseActiveStatus(Long studentAllergyId, boolean active);
    StudentCondition updateConditionActiveStatus(Long studentAllergyId, boolean active);
    StudentAllergy updateStudentAllergy(UpdateStudentAllergyRequest request);
    StudentDisease updateStudentDisease(UpdateStudentDiseaseRequest request);
    StudentCondition updateStudentCondition(UpdateStudentConditionRequest request);

    void updateBasicHealthData(StudentBasicHealthDataRequest request);
}
