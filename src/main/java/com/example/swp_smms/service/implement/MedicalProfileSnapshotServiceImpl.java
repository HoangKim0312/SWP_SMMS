package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.MedicalProfileSnapshot;
import com.example.swp_smms.repository.MedicalProfileRepository;
import com.example.swp_smms.repository.MedicalProfileSnapshotRepository;
import com.example.swp_smms.service.MedicalProfileSnapshotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MedicalProfileSnapshotServiceImpl implements MedicalProfileSnapshotService {

    private final MedicalProfileRepository medicalProfileRepository;
    private final MedicalProfileSnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void saveSnapshot(Long medicalProfileId) {
        MedicalProfile profile = medicalProfileRepository.findById(medicalProfileId)
                .orElseThrow(() -> new RuntimeException("Medical profile not found with id: " + medicalProfileId));

        Map<String, Object> snapshotMap = new HashMap<>();
        snapshotMap.put("basicHealth", profile.getBasicHealthData());
        snapshotMap.put("allergies", profile.getAllergies());
        snapshotMap.put("diseases", profile.getDiseases());
        snapshotMap.put("conditions", profile.getConditions());
        snapshotMap.put("externalVaccines", profile.getExternalVaccines());

        String snapshotJson;
        try {
            snapshotJson = objectMapper.writeValueAsString(snapshotMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize medical profile snapshot", e);
        }

        MedicalProfileSnapshot snapshot = new MedicalProfileSnapshot();
        snapshot.setMedicalProfile(profile);
        snapshot.setSnapshotTime(LocalDateTime.now());
        snapshot.setSnapshotData(snapshotJson);

        snapshotRepository.save(snapshot);
    }
}
