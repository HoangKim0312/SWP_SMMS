package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "MedicalProfileSnapshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProfileSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_profile_id")
    private MedicalProfile medicalProfile;

    @Column(name = "snapshot_time")
    private LocalDateTime snapshotTime;

    @Column(name = "snapshot_data", columnDefinition = "TEXT")
    private String snapshotData; // serialized JSON or string summary
}
