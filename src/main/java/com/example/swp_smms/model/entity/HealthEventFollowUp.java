package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HealthEventFollowUp")
public class HealthEventFollowUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private HealthEvent healthEvent;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "account_id")
    private Account parent;

    @Column(name = "instruction")
    private String instruction;

    @Column(name = "requires_doctor")
    private Boolean requiresDoctor;

    @Column(name = "status")
    private String status;
} 