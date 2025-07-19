package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MedicalProfile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_profile_id")
    private Long medicalProfileId;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id", nullable = false, unique = true)
    @JsonIgnore
    private Account student;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "medicalProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAllergy> allergies;

    @OneToMany(mappedBy = "medicalProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentDisease> diseases;

    @OneToMany(mappedBy = "medicalProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentCondition> conditions;

    @OneToOne(mappedBy = "medicalProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private StudentBasicHealthData basicHealthData;

    @OneToMany(mappedBy = "medicalProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MedicalProfileSnapshot> historySnapshots;
}
