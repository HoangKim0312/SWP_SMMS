package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "health_event_medication")
public class HealthEventMedication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_event_medication_id")
    private Long healthEventMedicationId;
    
    @ManyToOne
    @JoinColumn(name = "health_event_id", referencedColumnName = "event_id")
    private HealthEvent healthEvent;
    
    @ManyToOne
    @JoinColumn(name = "medication_id", referencedColumnName = "medication_id")
    private Medication medication;
    
    @Column(name = "dosage_amount")
    private Integer dosageAmount;
    
    @Column(name = "dosage_unit")
    private String dosageUnit; // e.g., "tablet", "ml", "mg"
    
    @Column(name = "frequency")
    private String frequency; // e.g., "3 times daily", "once daily"
    
    @Column(name = "duration")
    private String duration; // e.g., "7 days", "until symptoms improve"
    
    @Column(name = "administration_notes")
    private String administrationNotes; // e.g., "Take with food", "Apply to affected area"
    
    @Column(name = "usage_date")
    private String usageDate;
    
    @Column(name = "usage_time")
    private String usageTime; // e.g., "09:00", "after_meal"
} 