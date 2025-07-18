package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "StudentCondition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_profile_id")
    @JsonIgnore
    private MedicalProfile medicalProfile;

    @ManyToOne
    @JoinColumn(name = "condition_id")
    private SyndromeDisability syndromeDisability;

    @Column(name = "note")
    private String note;
}
