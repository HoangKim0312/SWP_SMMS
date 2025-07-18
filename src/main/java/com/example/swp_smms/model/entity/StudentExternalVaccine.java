package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "StudentExternalVaccine")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentExternalVaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_profile_id", nullable = false)
    private MedicalProfile medicalProfile;

    @ManyToOne
    @JoinColumn(name = "vaccine_id", nullable = false)
    private Vaccine vaccine;

    @Column(name = "injection_date", nullable = false)
    private LocalDate injectionDate;

    @Column(name = "location")
    private String location; // e.g., private clinic name

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "submitted_by")
    private String submittedBy; // name of parent or nurse

    @Column(name = "verified")
    private boolean verified = false; // for nurse/admin to verify authenticity
}
