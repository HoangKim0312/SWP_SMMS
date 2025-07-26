package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.StudentDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentDiseaseRepository extends JpaRepository<StudentDisease, Long> {

    List<StudentDisease> findByMedicalProfileAndActiveTrue(MedicalProfile profile);
}
