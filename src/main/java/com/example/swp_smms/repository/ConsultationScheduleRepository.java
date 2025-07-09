package com.example.swp_smms.repository;

import com.example.swp_smms.model.entity.ConsultationSchedule;
import com.example.swp_smms.model.enums.ConsultationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationScheduleRepository extends JpaRepository<ConsultationSchedule, Long> {
    List<ConsultationSchedule> findByStaff_AccountIdAndScheduledDate(UUID staffId, LocalDate scheduledDate);
    List<ConsultationSchedule> findByStaff_AccountIdAndScheduledDateAndSlot(UUID staffId, LocalDate scheduledDate, ConsultationSlot slot);
    List<ConsultationSchedule> findByScheduledDateOrderByScheduledDateAsc(LocalDate scheduledDate);
    List<ConsultationSchedule> findByScheduledDateOrderByScheduledDateDesc(LocalDate scheduledDate);
    List<ConsultationSchedule> findByStaff_AccountId(UUID staffId);
    List<ConsultationSchedule> findByStudent_AccountId(UUID studentId);
    List<ConsultationSchedule> findByParent_AccountId(UUID parentId);
} 