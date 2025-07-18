package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.MedicalProfileSnapshot;
import com.example.swp_smms.repository.MedicalProfileRepository;
import com.example.swp_smms.repository.MedicalProfileSnapshotRepository;
import com.example.swp_smms.service.SnapshotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SnapshotServiceImpl implements SnapshotService {

    private final MedicalProfileRepository medicalProfileRepository;
    private final MedicalProfileSnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public SnapshotServiceImpl(MedicalProfileRepository medicalProfileRepository,
                               MedicalProfileSnapshotRepository snapshotRepository,
                               ObjectMapper objectMapper) {
        this.medicalProfileRepository = medicalProfileRepository;
        this.snapshotRepository = snapshotRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public MedicalProfileSnapshot createSnapshot(Long medicalProfileId) throws Exception {
        MedicalProfile profile = medicalProfileRepository.findById(medicalProfileId)
                .orElseThrow(() -> new RuntimeException("MedicalProfile not found"));

        // Optional: Map to DTO here if you don’t want full entity in JSON
        String json = objectMapper.writeValueAsString(profile);

        MedicalProfileSnapshot snapshot = new MedicalProfileSnapshot();
        snapshot.setMedicalProfile(profile);
        snapshot.setSnapshotTime(LocalDateTime.now());
        snapshot.setSnapshotData(json);

        return snapshotRepository.save(snapshot);
    }
}
