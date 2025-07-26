package com.example.swp_smms.model.entity;

import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import com.example.swp_smms.model.enums.HealthEventPriority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private HealthEventPriority priority;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "parent_approval_status")
    private HealthEventApprovalStatus parentApprovalStatus;
    
    @Column(name = "parent_approval_reason")
    private String parentApprovalReason;
    
    @Column(name = "parent_approval_date")
    private LocalDateTime parentApprovalDate;
    
    @ManyToOne
    @JoinColumn(name = "approved_by_parent_id", referencedColumnName = "account_id")
    private Account approvedByParent;
    
    @Column(name = "requires_home_care")
    private Boolean requiresHomeCare;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "healthEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthEventMedication> medications;
    
    @OneToMany(mappedBy = "healthEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthEventFollowUp> followUps;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (parentApprovalStatus == null) {
            parentApprovalStatus = HealthEventApprovalStatus.NOT_REQUIRED;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 