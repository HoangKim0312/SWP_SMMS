package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Dosage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dosage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dosage_id")
    private Long dosageId;

    @ManyToOne
    @JoinColumn(name = "medication_sent_id", nullable = false)
    private MedicationSent medicationSent;

    @Column(name = "timing_notes", nullable = false)
    private String timingNotes; // e.g., "Before lunch", "After dinner"

    @OneToMany(mappedBy = "dosage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationItem> medicationItems;
}
