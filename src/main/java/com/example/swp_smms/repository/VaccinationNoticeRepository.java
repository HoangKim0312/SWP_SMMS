package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.VaccinationNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationNoticeRepository extends JpaRepository<VaccinationNotice, Long> {
    List<VaccinationNotice> findByVaccineNameContainingIgnoreCase(String vaccineName);
} 