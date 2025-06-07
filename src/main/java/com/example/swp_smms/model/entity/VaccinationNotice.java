package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    @Column(name = "vaccine_name")
    private String vaccineName;
    
    @Column(name = "date")
    private String date;
    
    @Column(name = "created_at")
    private String createdAt;
} 