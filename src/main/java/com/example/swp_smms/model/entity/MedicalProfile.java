package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MedicalProfile")
public class MedicalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_profile_id")
    private Long medicalProfileId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "chronic_diseases")
    private String chronicDiseases;

    @Column(name = "past_treatments")
    private String pastTreatments;

    @Column(name = "vision_status_left")
    private String visionStatusLeft;

    @Column(name = "vision_status_right")
    private String visionStatusRight;

    @Column(name = "hearing_status")
    private String hearingStatus;

    @Column(name = "immunization_status")
    private String immunizationStatus;


    @Column(name = "is_active")
    private boolean isActive = true; // Default to true

    //yyyy-MM-dd
    @Column(name = "last_updated")
    private String lastUpdated;
}
