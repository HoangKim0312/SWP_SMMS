package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthCheckNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HealthCheckNoticeRepository extends JpaRepository<HealthCheckNotice, Long> {
    List<HealthCheckNotice> findByCheckNoticeId(Long checkNoticeId);
} 