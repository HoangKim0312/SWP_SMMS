package com.example.swp_smms.model.entity;

import com.example.swp_smms.model.enums.MedicationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medication")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long medicationId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "quantity_type")
    private String quantityType; // viên, hộp, tuýp, lọ, etc.

    @Enumerated(EnumType.STRING)
    @Column(name = "medication_type")
    private MedicationType medicationType; // MEDICATION or MEDICAL_SUPPLY

    @Column(name = "expiry_date")
    private String expiryDate;
} 