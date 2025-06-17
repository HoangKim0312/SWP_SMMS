package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByNameContainingIgnoreCase(String name);
    List<Medication> findByQuantityLessThan(Integer quantity);
} 