package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.MedicalProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MedicalProfileServiceImpl implements MedicalProfileService {

    private final AccountRepository accountRepository;
    private final MedicalProfileRepository medicalProfileRepository;
    private final AllergenRepository allergenRepository;
    private final DiseaseRepository diseaseRepository;
    private final SyndromeDisabilityRepository conditionRepository;

    public MedicalProfileResponse mapToResponse(MedicalProfile profile) {
        MedicalProfileResponse response = new MedicalProfileResponse();
        response.setMedicalProfileId(profile.getMedicalProfileId());
        response.setActive(profile.isActive());
        response.setLastUpdated(profile.getLastUpdated());
        response.setStudentId(profile.getStudent().getAccountId());

        response.setAllergies(profile.getAllergies().stream().map(allergy -> {
            MedicalProfileResponse.AllergyDTO dto = new MedicalProfileResponse.AllergyDTO();
            dto.setAllergenId(allergy.getAllergen().getAllergenId());
            dto.setAllergenName(allergy.getAllergen().getName());
            dto.setReaction(allergy.getReaction());
            dto.setSeverity(allergy.getSeverity());
            dto.setLifeThreatening(allergy.isLifeThreatening());
            return dto;
        }).toList());

        response.setDiseases(profile.getDiseases().stream().map(disease -> {
            MedicalProfileResponse.DiseaseDTO dto = new MedicalProfileResponse.DiseaseDTO();
            dto.setDiseaseId(disease.getDisease().getDiseaseId());
            dto.setDiseaseName(disease.getDisease().getName());
            dto.setSinceDate(disease.getSinceDate());
            dto.setSeverity(disease.getSeverity());
            return dto;
        }).toList());

        response.setConditions(profile.getConditions().stream().map(cond -> {
            MedicalProfileResponse.ConditionDTO dto = new MedicalProfileResponse.ConditionDTO();
            dto.setConditionId(cond.getSyndromeDisability().getConditionId());
            dto.setConditionName(cond.getSyndromeDisability().getName());
            dto.setNote(cond.getNote());
            return dto;
        }).toList());

        StudentBasicHealthData bhd = profile.getBasicHealthData();
        if (bhd != null) {
            MedicalProfileResponse.BasicHealthDataDTO dto = new MedicalProfileResponse.BasicHealthDataDTO();
            dto.setHeightCm(bhd.getHeightCm());
            dto.setWeightKg(bhd.getWeightKg());
            dto.setVisionLeft(bhd.getVisionLeft());
            dto.setVisionRight(bhd.getVisionRight());
            dto.setHearingStatus(bhd.getHearingStatus());
            dto.setGender(bhd.getGender());
            dto.setBloodType(bhd.getBloodType());
            dto.setLastMeasured(bhd.getLastMeasured());
            response.setBasicHealthData(dto);
        }

        return response;
    }


    @Override
    public MedicalProfile createMedicalProfile(MedicalProfileRequest request) {
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MedicalProfile profile = new MedicalProfile();
        profile.setStudent(student);
        profile.setActive(true);
        profile.setLastUpdated(LocalDateTime.now());

        // Build allergy list
        List<StudentAllergy> allergies = request.getAllergies().stream().map(dto -> {
            Allergen allergen = allergenRepository.findById(dto.getAllergenId())
                    .orElseThrow(() -> new RuntimeException("Allergen not found"));
            StudentAllergy allergy = new StudentAllergy();
            allergy.setAllergen(allergen);
            allergy.setReaction(dto.getReaction());
            allergy.setSeverity(dto.getSeverity());
            allergy.setLifeThreatening(dto.isLifeThreatening());
            allergy.setMedicalProfile(profile);
            return allergy;
        }).toList();

        // Build disease list
        List<StudentDisease> diseases = request.getDiseases().stream().map(dto -> {
            Disease disease = diseaseRepository.findById(dto.getDiseaseId())
                    .orElseThrow(() -> new RuntimeException("Disease not found"));
            StudentDisease sd = new StudentDisease();
            sd.setDisease(disease);
            sd.setSinceDate(dto.getSinceDate());
            sd.setSeverity(dto.getSeverity());
            sd.setMedicalProfile(profile);
            return sd;
        }).toList();

        // Build conditions
        List<StudentCondition> conditions = request.getConditions().stream().map(dto -> {
            SyndromeDisability condition = conditionRepository.findById(dto.getConditionId())
                    .orElseThrow(() -> new RuntimeException("Condition not found"));
            StudentCondition sc = new StudentCondition();
            sc.setSyndromeDisability(condition);
            sc.setNote(dto.getNote());
            sc.setMedicalProfile(profile);
            return sc;
        }).toList();

        // Basic health data
        StudentBasicHealthData basicData = new StudentBasicHealthData();
        MedicalProfileRequest.BasicHealthDataDTO bd = request.getBasicHealthData();
        basicData.setMedicalProfile(profile);
        basicData.setHeightCm(bd.getHeightCm());
        basicData.setWeightKg(bd.getWeightKg());
        basicData.setVisionLeft(bd.getVisionLeft());
        basicData.setVisionRight(bd.getVisionRight());
        basicData.setHearingStatus(bd.getHearingStatus());
        basicData.setGender(bd.getGender());
        basicData.setBloodType(bd.getBloodType());
        basicData.setLastMeasured(bd.getLastMeasured());

        // Set to profile
        profile.setAllergies(allergies);
        profile.setDiseases(diseases);
        profile.setConditions(conditions);
        profile.setBasicHealthData(basicData);

        return medicalProfileRepository.save(profile);
    }

}
