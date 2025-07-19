package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.SnapshotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

    private final MedicalProfileRepository medicalProfileRepository;
    private final MedicalProfileSnapshotRepository snapshotRepository;
    private final StudentAllergyRepository studentAllergyRepository;
    private final StudentDiseaseRepository studentDiseaseRepository;
    private final StudentConditionRepository studentConditionRepository;
    private final ObjectMapper objectMapper;

    @Data
    // AllergenSnapshot.java
    public class AllergenSnapshot {
        private Long allergenId;
        private String name;
    }

    @Data
    // StudentAllergySnapshot.java
    public class StudentAllergySnapshot {
        private Long studentAllergyId;
        private AllergenSnapshot allergen;
        private String reaction;
        private int severity;
        private boolean lifeThreatening;
    }

    @Data
    // DiseaseSnapshot.java
    public class DiseaseSnapshot {
        private Long diseaseId;
        private String name;
    }

    @Data
    // StudentDiseaseSnapshot.java
    public class StudentDiseaseSnapshot {
        private Long id;
        private DiseaseSnapshot disease;
        private String sinceDate;
        private int severity;
    }

    @Data
    // ConditionSnapshot.java
    public class ConditionSnapshot {
        private Long conditionId;
        private String name;
    }

    @Data
    // StudentConditionSnapshot.java
    public class StudentConditionSnapshot {
        private Long id;
        private ConditionSnapshot syndromeDisability;
        private String note;
    }

    @Override
    public MedicalProfileSnapshot createSnapshot(Long medicalProfileId) throws Exception {
        MedicalProfile profile = medicalProfileRepository.findById(medicalProfileId)
                .orElseThrow(() -> new RuntimeException("MedicalProfile not found"));

        // --- Filter and transform ---
        List<StudentAllergySnapshot> allergySnapshots = profile.getAllergies().stream()
                .filter(StudentAllergy::isActive)
                .map(a -> {
                    StudentAllergySnapshot snapshot = new StudentAllergySnapshot();
                    snapshot.setStudentAllergyId(a.getStudentAllergyId());
                    snapshot.setReaction(a.getReaction());
                    snapshot.setSeverity(a.getSeverity());
                    snapshot.setLifeThreatening(a.isLifeThreatening());

                    AllergenSnapshot allergenSnapshot = new AllergenSnapshot();
                    allergenSnapshot.setAllergenId(a.getAllergen().getAllergenId());
                    allergenSnapshot.setName(a.getAllergen().getName());

                    snapshot.setAllergen(allergenSnapshot);
                    return snapshot;
                }).toList();

        List<StudentDiseaseSnapshot> diseaseSnapshots = profile.getDiseases().stream()
                .filter(StudentDisease::isActive)
                .map(d -> {
                    StudentDiseaseSnapshot snapshot = new StudentDiseaseSnapshot();
                    snapshot.setId(d.getId());
                    snapshot.setSinceDate(d.getSinceDate().toString());
                    snapshot.setSeverity(d.getSeverity());

                    DiseaseSnapshot disease = new DiseaseSnapshot();
                    disease.setDiseaseId(d.getDisease().getDiseaseId());
                    disease.setName(d.getDisease().getName());

                    snapshot.setDisease(disease);
                    return snapshot;
                }).toList();

        List<StudentConditionSnapshot> conditionSnapshots = profile.getConditions().stream()
                .filter(StudentCondition::isActive)
                .map(c -> {
                    StudentConditionSnapshot snapshot = new StudentConditionSnapshot();
                    snapshot.setId(c.getId());
                    snapshot.setNote(c.getNote());

                    ConditionSnapshot condition = new ConditionSnapshot();
                    condition.setConditionId(c.getSyndromeDisability().getConditionId());
                    condition.setName(c.getSyndromeDisability().getName());

                    snapshot.setSyndromeDisability(condition);
                    return snapshot;
                }).toList();

        // You can create a new DTO combining all these into one "snapshot payload" object:
        Map<String, Object> snapshotData = new LinkedHashMap<>();
        snapshotData.put("allergies", allergySnapshots);
        snapshotData.put("diseases", diseaseSnapshots);
        snapshotData.put("conditions", conditionSnapshots);
        snapshotData.put("basicHealthData", profile.getBasicHealthData()); // Full

        String json = objectMapper.writeValueAsString(snapshotData);

        MedicalProfileSnapshot snapshot = new MedicalProfileSnapshot();
        snapshot.setMedicalProfile(profile);
        snapshot.setSnapshotTime(LocalDateTime.now());
        snapshot.setSnapshotData(json);


//        snapshot.setMedicalProfile(null); // ← Do this before returning
        return snapshotRepository.save(snapshot);
    }


}
