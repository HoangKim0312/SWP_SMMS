package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.StudentExternalVaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentExternalVaccineRepository extends JpaRepository<StudentExternalVaccine, Long> {
    List<StudentExternalVaccine> findByStudent_AccountIdAndVerified(UUID studentId, boolean verified);

}
