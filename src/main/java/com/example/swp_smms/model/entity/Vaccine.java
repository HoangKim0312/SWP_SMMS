package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Vaccine")
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id")
    private Long vaccineId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "version")
    private String version;
    
    @Column(name = "release_date")
    private String releaseDate;
    
    @Column(name = "confirmed_at")
    private String confirmedAt;
} 