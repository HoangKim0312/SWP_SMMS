package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.StudentCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentConditionRepository extends JpaRepository<StudentCondition, Long> {

    List<StudentCondition> findByMedicalProfileAndActiveTrue(MedicalProfile profile);
}
