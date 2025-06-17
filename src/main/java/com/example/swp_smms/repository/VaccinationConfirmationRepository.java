package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.VaccinationConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccinationConfirmationRepository extends JpaRepository<VaccinationConfirmation, Long> {
    List<VaccinationConfirmation> findByStudent_AccountId(UUID studentId);
    List<VaccinationConfirmation> findByParent_AccountId(UUID parentId);
    List<VaccinationConfirmation> findByVaccinationNotice_VaccineNoticeId(Long vaccineNoticeId);
    List<VaccinationConfirmation> findByStatus(String status);
} 