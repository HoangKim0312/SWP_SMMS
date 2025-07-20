package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.payload.request.*;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.MedicalProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalProfileServiceImpl implements MedicalProfileService {

    private final AccountRepository accountRepository;
    private final MedicalProfileRepository medicalProfileRepository;
    private final AllergenRepository allergenRepository;
    private final DiseaseRepository diseaseRepository;
    private final SyndromeDisabilityRepository conditionRepository;
    private final StudentAllergyRepository studentAllergyRepository;
    private final StudentDiseaseRepository studentDiseaseRepository;
    private final SyndromeDisabilityRepository syndromeDisabilityRepository;
    private final StudentConditionRepository studentConditionRepository;

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
    public MedicalProfileResponse getFullMedicalProfile(String studentId) {
        UUID studentUUID = UUID.fromString(studentId);

        Account student = accountRepository.findById(studentUUID)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MedicalProfile profile = medicalProfileRepository.findByStudentAndActiveTrue(student)
                .orElseThrow(() -> new RuntimeException("Active medical profile not found"));

        MedicalProfileResponse response = new MedicalProfileResponse();
        response.setMedicalProfileId(profile.getMedicalProfileId());
        response.setActive(profile.isActive());
        response.setLastUpdated(profile.getLastUpdated());
        response.setStudentId(student.getAccountId());

        // Basic Health Data
        if (profile.getBasicHealthData() != null) {
            var bh = profile.getBasicHealthData();
            MedicalProfileResponse.BasicHealthDataDTO bhDto = new MedicalProfileResponse.BasicHealthDataDTO();
            bhDto.setHeightCm(bh.getHeightCm());
            bhDto.setWeightKg(bh.getWeightKg());
            bhDto.setVisionLeft(bh.getVisionLeft());
            bhDto.setVisionRight(bh.getVisionRight());
            bhDto.setHearingStatus(bh.getHearingStatus());
            bhDto.setGender(bh.getGender());
            bhDto.setBloodType(bh.getBloodType());
            bhDto.setLastMeasured(bh.getLastMeasured() != null ? bh.getLastMeasured().toString() : null);
            response.setBasicHealthData(bhDto);
        }

        // Allergies
        List<MedicalProfileResponse.AllergyDTO> allergyDtos = profile.getAllergies().stream()
                .filter(StudentAllergy::isActive)
                .map(allergy -> {
                    var dto = new MedicalProfileResponse.AllergyDTO();
                    dto.setAllergenId(allergy.getAllergen().getAllergenId());
                    dto.setAllergenName(allergy.getAllergen().getName());
                    dto.setReaction(allergy.getReaction());
                    dto.setSeverity(allergy.getSeverity());
                    dto.setLifeThreatening(allergy.isLifeThreatening());
                    return dto;
                })
                .toList();
        response.setAllergies(allergyDtos);

        // Diseases
        List<MedicalProfileResponse.DiseaseDTO> diseaseDtos = profile.getDiseases().stream()
                .filter(StudentDisease::isActive)
                .map(disease -> {
                    var dto = new MedicalProfileResponse.DiseaseDTO();
                    dto.setDiseaseId(disease.getDisease().getDiseaseId());
                    dto.setDiseaseName(disease.getDisease().getName());
                    dto.setSeverity(disease.getSeverity());
                    dto.setSinceDate(disease.getSinceDate() != null ? disease.getSinceDate().toString() : null);
                    return dto;
                })
                .toList();
        response.setDiseases(diseaseDtos);

        // Conditions
        List<MedicalProfileResponse.ConditionDTO> conditionDtos = profile.getConditions().stream()
                .filter(StudentCondition::isActive)
                .map(condition -> {
                    var dto = new MedicalProfileResponse.ConditionDTO();
                    dto.setConditionId(condition.getSyndromeDisability().getConditionId());
                    dto.setConditionName(condition.getSyndromeDisability().getName());
                    dto.setNote(condition.getNote());
                    return dto;
                })
                .toList();
        response.setConditions(conditionDtos);

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
            allergy.setActive(true);
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
            sd.setActive(true);
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
            sc.setActive(true);
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

    @Override
    public StudentAllergy addAllergyToStudentProfile(AddAllergyToMedicalProfileRequest request) {
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MedicalProfile profile = medicalProfileRepository.findByStudentAndActiveTrue(student)
                .orElseThrow(() -> new RuntimeException("Active medical profile not found"));

        Allergen allergen = allergenRepository.findById(request.getAllergenId())
                .orElseThrow(() -> new RuntimeException("Allergen not found"));

        StudentAllergy newAllergy = new StudentAllergy();
        newAllergy.setMedicalProfile(profile);
        newAllergy.setAllergen(allergen);
        newAllergy.setReaction(request.getReaction());
        newAllergy.setSeverity(request.getSeverity());
        newAllergy.setLifeThreatening(request.isLifeThreatening());
        newAllergy.setActive(true);

        return studentAllergyRepository.save(newAllergy);
    }

    @Override
    public StudentDisease addStudentDisease(AddStudentDiseaseRequest request) {
        UUID studentUUID = UUID.fromString(request.getStudentId());

        Account student = accountRepository.findById(studentUUID)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MedicalProfile profile = medicalProfileRepository.findByStudentAndActiveTrue(student)
                .orElseThrow(() -> new RuntimeException("Active medical profile not found for student"));

        Disease disease = diseaseRepository.findById(request.getDiseaseId())
                .orElseThrow(() -> new RuntimeException("Disease not found"));

        StudentDisease studentDisease = new StudentDisease();
        studentDisease.setMedicalProfile(profile);
        studentDisease.setDisease(disease);
        studentDisease.setSinceDate(request.getSinceDate());
        studentDisease.setSeverity(request.getSeverity());
        studentDisease.setActive(true);

        return studentDiseaseRepository.save(studentDisease);
    }

    public StudentCondition addStudentCondition(AddStudentConditionRequest request) {
        UUID studentUUID = UUID.fromString(request.getStudentId());

        Account student = accountRepository.findById(studentUUID)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MedicalProfile profile = medicalProfileRepository.findByStudentAndActiveTrue(student)
                .orElseThrow(() -> new RuntimeException("Active medical profile not found"));

        SyndromeDisability condition = syndromeDisabilityRepository.findById(request.getConditionId())
                .orElseThrow(() -> new RuntimeException("Condition not found"));

        StudentCondition studentCondition = new StudentCondition();
        studentCondition.setMedicalProfile(profile);
        studentCondition.setSyndromeDisability(condition);
        studentCondition.setNote(request.getNote());
        studentCondition.setActive(true);

        return studentConditionRepository.save(studentCondition);
    }

}
