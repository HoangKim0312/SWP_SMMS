package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
    List<VaccinationRecord> findByStudent_AccountId(UUID studentId);
    List<VaccinationRecord> findByNurse_AccountId(UUID nurseId);
    List<VaccinationRecord> findByVaccinationNotice_VaccineNoticeId(Long vaccineNoticeId);
} 