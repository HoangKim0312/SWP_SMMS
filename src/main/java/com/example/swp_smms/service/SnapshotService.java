package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.MedicalProfileSnapshot;

public interface SnapshotService {
    MedicalProfileSnapshot createSnapshot(Long medicalProfileId) throws Exception;
}
