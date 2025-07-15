package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.VaccineBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineBatchRepository extends JpaRepository<VaccineBatch, Long> {
    List<VaccineBatch> findByVaccineId(Long vaccineId);
}
