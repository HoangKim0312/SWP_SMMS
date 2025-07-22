package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.StudentAllergy;
import com.example.swp_smms.model.entity.MedicalProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface StudentAllergyRepository extends JpaRepository<StudentAllergy, Long> {

    List<StudentAllergy> findByMedicalProfileAndActiveTrue(MedicalProfile profile);
    Optional<StudentAllergy> findById(Long id);

}
