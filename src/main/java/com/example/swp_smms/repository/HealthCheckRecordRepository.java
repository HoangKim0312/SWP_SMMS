package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthCheckRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HealthCheckRecordRepository extends JpaRepository<HealthCheckRecord, Long> {
    List<HealthCheckRecord> findByStudent_AccountId(UUID studentId);
    List<HealthCheckRecord> findByNurse_AccountId(UUID nurseId);
    List<HealthCheckRecord> findByRecordId(Long recordId);
    List<HealthCheckRecord> findByHealthCheckNotice_CheckNoticeId(Long checkNoticeId);
    List<HealthCheckRecord> findByDate(String date);
    List<HealthCheckRecord> findByHealthCheckNotice_TitleContainingIgnoreCase(String title);
} 