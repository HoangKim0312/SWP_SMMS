package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "StudentDisease")
public class StudentDisease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_profile_id", nullable = false)
    @JsonIgnore
    private MedicalProfile medicalProfile;

    @ManyToOne
    @JoinColumn(name = "disease_id", nullable = false)
    private Disease disease;

    @JoinColumn(name = "since_date", nullable = false)
    private String sinceDate;

    /**
     * Severity from 1 (mild) to 10 (severe)
     */
    @Column(name = "severity", nullable = false)
    private int severity;
}
