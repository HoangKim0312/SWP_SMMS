package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "health_event")
public class HealthEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;
    
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;
    
    @ManyToOne
    @JoinColumn(name = "nurse_id", referencedColumnName = "account_id")
    private Account nurse;
    
    @Column(name = "event_date")
    private String eventDate;
    
    @Column(name = "event_type")
    private String eventType;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "solution")
    private String solution;
    
    @Column(name = "note")
    private String note;
    
    @Column(name = "status")
    private String status;
} 