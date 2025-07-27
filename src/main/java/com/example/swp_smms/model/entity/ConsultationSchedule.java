package com.example.swp_smms.model.entity;

import com.example.swp_smms.model.enums.ConsultationSlot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultation_schedule")
public class ConsultationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    private Long consultationId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "account_id")
    private Account parent;

    @ManyToOne
    @JoinColumn(name = "nurse_id", referencedColumnName = "account_id")
    private Account nurse;

    @ManyToOne
    @JoinColumn(name = "health_check_record_id", referencedColumnName = "record_id")
    private HealthCheckRecord healthCheckRecord;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot")
    private ConsultationSlot slot;

    @Column(name = "status")
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reason")
    private String reason;
} 