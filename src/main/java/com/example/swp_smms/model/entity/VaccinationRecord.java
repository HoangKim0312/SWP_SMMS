package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VaccinationRecord")
public class VaccinationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;
    
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;
    
    @ManyToOne
    @JoinColumn(name = "nurse_id", referencedColumnName = "account_id")
    private Account nurse;
    
    @ManyToOne
    @JoinColumn(name = "vaccine_notice_id", referencedColumnName = "vaccine_notice_id")
    private VaccinationNotice vaccinationNotice;
    
    @Column(name = "results")
    private String results;
    
    @Column(name = "date")
    private String date;
} 