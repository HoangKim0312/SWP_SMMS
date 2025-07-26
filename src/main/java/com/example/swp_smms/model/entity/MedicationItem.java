package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medication_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_item_id")
    private Long medicationItemId;

    @ManyToOne
    @JoinColumn(name = "dosage_id", nullable = false)
    private Dosage dosage;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "amount", nullable = false)
    private Double amount; // e.g., 1 tablet

    @Column(name = "unit", nullable = false)
    private String unit; // e.g., viên, gram, mlg, ml

}
