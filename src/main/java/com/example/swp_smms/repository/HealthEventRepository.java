package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import com.example.swp_smms.model.enums.HealthEventPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface HealthEventRepository extends JpaRepository<HealthEvent, Long> {

    List<HealthEvent> findByEventDate(String eventDate);
    List<HealthEvent> findByPriority(HealthEventPriority priority);
    List<HealthEvent> findByStudent_AccountIdInAndParentApprovalStatus(List<UUID> studentIds, HealthEventApprovalStatus approvalStatus);
    List<HealthEvent> findByNurse_AccountId(UUID nurseId);
}
