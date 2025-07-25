package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.StudentBasicHealthData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentBasicHealthRepository extends JpaRepository<StudentBasicHealthData, Long> {
    Optional<StudentBasicHealthData> findByMedicalProfile(MedicalProfile profile);
}
