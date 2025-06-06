package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EventMedicationUsage")
public class EventMedicationUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long usageId;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private HealthEvent healthEvent;

    @ManyToOne
    @JoinColumn(name = "medication_id", referencedColumnName = "medication_id")
    private Medication medication;

    @Column(name = "quantity_used")
    private Integer quantityUsed;

    @Column(name = "time")
    private String time;

    @Column(name = "dosage")
    private String dosage;
} 