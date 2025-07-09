package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HealthCheckRecord")
public class HealthCheckRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "nurse_id", referencedColumnName = "account_id")
    private Account nurse;

    @ManyToOne
    @JoinColumn(name = "check_notice_id", referencedColumnName = "check_notice_id")
    private HealthCheckNotice healthCheckNotice;

    @Column(name = "results")
    private String results;

    @Column(name = "date")
    private LocalDate date;
} 