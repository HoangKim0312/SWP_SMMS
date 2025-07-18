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
    private Long id;

    @OneToOne
    @JoinColumn(name = "medical_profile_id")
    @JsonIgnore
    private MedicalProfile medicalProfile;

    private Double heightCm;
    private Double weightKg;
    private String visionLeft;
    private String visionRight;
    private String hearingStatus;
    private String gender;
    private String bloodType;

    private String lastMeasured; // yyyy-MM-dd
}
