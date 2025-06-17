package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.EventMedicationUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMedicationUsageRepository extends JpaRepository<EventMedicationUsage, Long> {
    List<EventMedicationUsage> findByHealthEvent_EventId(Long eventId);
    List<EventMedicationUsage> findByMedication_MedicationId(Long medicationId);
} 