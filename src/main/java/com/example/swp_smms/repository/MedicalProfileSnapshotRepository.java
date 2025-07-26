package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicalProfileSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface MedicalProfileSnapshotRepository extends JpaRepository<MedicalProfileSnapshot, Long> {
    Page<MedicalProfileSnapshot> findAllByMedicalProfile_Student_AccountId(UUID medicalProfileStudentAccountId, Pageable pageable);

}
