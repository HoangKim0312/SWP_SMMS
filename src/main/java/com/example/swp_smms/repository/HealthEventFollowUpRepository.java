package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthEventFollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthEventFollowUpRepository extends JpaRepository<HealthEventFollowUp, Long> {
    List<HealthEventFollowUp> findByHealthEvent_EventId(Long eventId);
    List<HealthEventFollowUp> findByParent_AccountId(java.util.UUID parentId);
} 