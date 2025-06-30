package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;
    
    @Column(name = "class_name")
    private String className;
    
    @Column(name = "description")
    private String description;

    @Column(name = "school_year")
    private int schoolYear;
    
    @OneToMany(mappedBy = "clazz")
    private List<Account> accounts;
} 