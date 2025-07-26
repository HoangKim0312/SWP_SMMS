package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.HealthEventMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthEventMedicationRepository extends JpaRepository<HealthEventMedication, Long> {
    List<HealthEventMedication> findByHealthEvent_EventId(Long eventId);
    void deleteByHealthEvent_EventId(Long eventId);
} 