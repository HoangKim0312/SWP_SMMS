package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicalProfileSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalProfileSnapshotRepository extends JpaRepository<MedicalProfileSnapshot, Long> {
}
