package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.payload.request.*;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.MedicalProfileService;
import com.example.swp_smms.service.SnapshotService;
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
    private final SnapshotService snapshotService;
    private final StudentBasicHealthRepository studentBasicHealthRepository;
    private void takeSnapshotForProfile(MedicalProfile profile) {
        try {
            snapshotService.createSnapshot(profile.getMedicalProfileId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create snapshot: " + e.getMessage(), e);
        }
    }


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
            bhDto.setStudentBasicHealthId(bh.getId());
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
                    dto.setStudentAllergyId(allergy.getStudentAllergyId());
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
        List<MedicalProfileResponse.DiseaseDTO> studentDiseaseDtos = profile.getDiseases().stream()
                .filter(StudentDisease::isActive)
                .map(studentDisease -> {
                    var dto = new MedicalProfileResponse.DiseaseDTO();
                    dto.setStudentDiseaseId(studentDisease.getId());
                    dto.setDiseaseId(studentDisease.getDisease().getDiseaseId());
                    dto.setDiseaseName(studentDisease.getDisease().getName());
                    dto.setSeverity(studentDisease.getSeverity());
                    dto.setSinceDate(studentDisease.getSinceDate() != null ? studentDisease.getSinceDate().toString() : null);
                    return dto;
                })
                .toList();
        response.setDiseases(studentDiseaseDtos);

        // Conditions
        List<MedicalProfileResponse.ConditionDTO> studentConditionDtos = profile.getConditions().stream()
                .filter(StudentCondition::isActive)
                .map(studentCondition -> {
                    var dto = new MedicalProfileResponse.ConditionDTO();
                    dto.setStudentConditionId(studentCondition.getId());
                    dto.setConditionId(studentCondition.getSyndromeDisability().getConditionId());
                    dto.setConditionName(studentCondition.getSyndromeDisability().getName());
                    dto.setNote(studentCondition.getNote());
                    return dto;
                })
                .toList();
        response.setConditions(studentConditionDtos);

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

        MedicalProfile savedProfile = medicalProfileRepository.save(profile);

        takeSnapshotForProfile(savedProfile); //automatically saves snapshot after creating

        return savedProfile;
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
        StudentAllergy savedAllergy = studentAllergyRepository.save(newAllergy);
        profile.setLastUpdated(LocalDateTime.now());
        takeSnapshotForProfile(profile);
        return savedAllergy;
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

        StudentDisease savedDisease = studentDiseaseRepository.save(studentDisease);
        profile.setLastUpdated(LocalDateTime.now());
        takeSnapshotForProfile(profile);
        return savedDisease;
    }

    @Override
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

        StudentCondition savedCondition = studentConditionRepository.save(studentCondition);
        profile.setLastUpdated(LocalDateTime.now());
        takeSnapshotForProfile(profile);
        return savedCondition;
    }


    @Override
    public StudentAllergy updateAllergyActiveStatus(Long studentAllergyId, boolean active) {
        StudentAllergy allergy = studentAllergyRepository.findById(studentAllergyId)
                .orElseThrow(() -> new RuntimeException("StudentAllergy not found with ID: " + studentAllergyId));
        allergy.setActive(active);
        StudentAllergy saved = studentAllergyRepository.save(allergy);
        allergy.getMedicalProfile().setLastUpdated(LocalDateTime.now());
        takeSnapshotForProfile(allergy.getMedicalProfile());
        return saved;

    }

    @Override
    public StudentDisease updateDiseaseActiveStatus(Long id, boolean active) {
        StudentDisease disease = studentDiseaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudentDisease not found with ID: " + id));
        disease.setActive(active);
        StudentDisease saved = studentDiseaseRepository.save(disease);
        disease.getMedicalProfile().setLastUpdated(LocalDateTime.now());
        takeSnapshotForProfile(disease.getMedicalProfile());
        return saved;
    }

    @Override
    public StudentCondition updateConditionActiveStatus(Long id, boolean active) {
        StudentCondition condition = studentConditionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudentCondition not found with ID: " + id));
        condition.setActive(active);
        StudentCondition saved = studentConditionRepository.save(condition);
        condition.getMedicalProfile().setLastUpdated(LocalDateTime.now());
        takeSnapshotForProfile(condition.getMedicalProfile());
        return saved;

    }

    @Override
    public StudentAllergy updateStudentAllergy(UpdateStudentAllergyRequest request) {
        StudentAllergy allergy = studentAllergyRepository.findById(request.getStudentAllergyId())
                .orElseThrow(() -> new RuntimeException("StudentAllergy not found with ID: " + request.getStudentAllergyId()));

        allergy.setReaction(request.getReaction());
        allergy.setSeverity(request.getSeverity());
        allergy.setLifeThreatening(request.isLifeThreatening());

        StudentAllergy saved = studentAllergyRepository.save(allergy);
        allergy.getMedicalProfile().setLastUpdated(LocalDateTime.now());
        takeSnapshotForProfile(allergy.getMedicalProfile());
        return saved;
    }
    @Override
    public StudentDisease updateStudentDisease(UpdateStudentDiseaseRequest request) {
        StudentDisease disease = studentDiseaseRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("StudentDisease not found with ID: " + request.getId()));

        disease.setSeverity(request.getSeverity());
        disease.getMedicalProfile().setLastUpdated(LocalDateTime.now());

        StudentDisease saved = studentDiseaseRepository.save(disease);
        takeSnapshotForProfile(disease.getMedicalProfile());
        return saved;
    }
    @Override
    public StudentCondition updateStudentCondition(UpdateStudentConditionRequest request) {
        StudentCondition condition = studentConditionRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("StudentCondition not found with ID: " + request.getId()));


        condition.setNote(request.getNote());

        StudentCondition saved = studentConditionRepository.save(condition);
        condition.getMedicalProfile().setLastUpdated(LocalDateTime.now());

        takeSnapshotForProfile(condition.getMedicalProfile());
        return saved;
    }

    @Override
    public void updateBasicHealthData(StudentBasicHealthDataRequest request) {
        // 1. Get student's medical profile
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MedicalProfile profile = medicalProfileRepository.findByStudentAndActiveTrue(student)
                .orElseThrow(() -> new RuntimeException("Medical profile not found"));

        // 2. Get or create StudentBasicHealthData
        StudentBasicHealthData healthData = studentBasicHealthRepository
                .findByMedicalProfile(profile)
                .orElse(new StudentBasicHealthData());

        // 3. Update fields
        healthData.setMedicalProfile(profile);
        healthData.setHeightCm(request.getHeightCm());
        healthData.setWeightKg(request.getWeightKg());
        healthData.setVisionLeft(request.getVisionLeft());
        healthData.setVisionRight(request.getVisionRight());
        healthData.setHearingStatus(request.getHearingStatus());
        healthData.setGender(request.getGender());
        healthData.setBloodType(request.getBloodType());
        healthData.setLastMeasured(LocalDateTime.now().toString());

        //4. save new update time for profile
        profile.setLastUpdated(LocalDateTime.now());

        // 5. Save
        studentBasicHealthRepository.save(healthData);
        takeSnapshotForProfile(profile);
    }

}
