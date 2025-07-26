package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.MedicalProfileSnapshot;
import com.example.swp_smms.model.payload.response.MedicalProfileSnapshotResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SnapshotService {
    MedicalProfileSnapshot createSnapshot(Long medicalProfileId) throws Exception;
    Page<MedicalProfileSnapshotResponse> getSnapshotsByStudentId(UUID studentId, Pageable pageable);
}
