package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "StudentBasicHealthData")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBasicHealthData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_health_data_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "medical_profile_id", nullable = false)
    @JsonIgnore
    private MedicalProfile medicalProfile;

    @Column(name = "height_cm")
    private Double heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "vision_left")
    private String visionLeft;

    @Column(name = "vision_right")
    private String visionRight;

    @Column(name = "hearing_status")
    private String hearingStatus;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "last_measured")
    private String lastMeasured; // Format: yyyy-MM-dd
}
