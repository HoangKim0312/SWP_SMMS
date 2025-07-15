package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VaccinationNotice")
public class VaccinationNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_notice_id")
    private Long vaccineNoticeId;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;

    //for students in selected grade
    @Column(name = "grade")
    private int grade;

    @Column(name = "vaccine_name")
    private String vaccineName;

    @ManyToOne
    @JoinColumn(name = "vaccine_batch_id")
    private VaccineBatch vaccineBatch;

    @Column(name = "vaccination_date")
    private LocalDate vaccinationDate;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
} 