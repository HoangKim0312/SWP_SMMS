package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthCheckConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface HealthCheckConfirmationRepository extends JpaRepository<HealthCheckConfirmation, Long> {
    // Add custom query methods if needed
    List<HealthCheckConfirmation> findByStudent_AccountId(UUID studentId);
    List<HealthCheckConfirmation> findByParent_AccountId(UUID parentId);
    List<HealthCheckConfirmation> findByHealthCheckNotice_CheckNoticeId(Long checkNoticeId);
    List<HealthCheckConfirmation> findByStatus(String status);
    List<HealthCheckConfirmation> findByConfirmationId(Long confirmationId);
    List<HealthCheckConfirmation> findByConfirmedAt(String confirmedAt);
    List<HealthCheckConfirmation> findByHealthCheckNotice_TitleContainingIgnoreCase(String title);
} 