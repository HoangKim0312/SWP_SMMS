package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HealthCheckConfirmation")
public class HealthCheckConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "confirmation_id")
    private Long confirmationId;

    @ManyToOne
    @JoinColumn(name = "check_notice_id", referencedColumnName = "check_notice_id")
    private HealthCheckNotice healthCheckNotice;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "account_id")
    private Account parent;

    @Column(name = "status")
    private String status;

    @Column(name = "confirmed_at")
    private String confirmedAt;
} 