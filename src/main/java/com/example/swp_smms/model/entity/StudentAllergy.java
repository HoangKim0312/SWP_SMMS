package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_allergy")
public class StudentAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_allergy_id")
    private Long studentAllergyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_profile_id", nullable = false)
    @JsonIgnore
    private MedicalProfile medicalProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergen_id", nullable = false)
    private Allergen allergen;

    @Column(name = "reaction", columnDefinition = "TEXT")
    private String reaction;

    /**
     * Severity from 1 (mild) to 10 (severe)
     */
    @Column(name = "severity", nullable = false)
    private int severity;

    @Column(name = "is_life_threatening", nullable = false)
    private boolean isLifeThreatening;

    @Column(name = "active", nullable = false)
    private boolean active = true;

}
