package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthCheckNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface HealthCheckNoticeRepository extends JpaRepository<HealthCheckNotice, Long> {
    List<HealthCheckNotice> findByDate(LocalDate date);
    List<HealthCheckNotice> findByTitleContainingIgnoreCase(String title);
    List<HealthCheckNotice> findByGrade(Integer grade);
    List<HealthCheckNotice> findByPriority(String priority);
} 