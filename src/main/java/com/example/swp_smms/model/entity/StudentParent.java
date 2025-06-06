package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Student_Parent")
public class StudentParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "account_id")
    private Account parent;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;
} 