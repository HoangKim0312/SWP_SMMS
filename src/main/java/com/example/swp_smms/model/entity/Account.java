package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private UUID accountId;
    
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "dob")
    private String dob;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "locked")
    private boolean locked = false;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;

    @OneToMany(mappedBy = "student")
    private List<MedicalProfile> medicalProfiles;
    
    @OneToMany(mappedBy = "student")
    private List<HealthEvent> healthEvents;
    
    @OneToMany(mappedBy = "nurse")
    private List<HealthEvent> nurseHealthEvents;
    
    @OneToMany(mappedBy = "student")
    private List<VaccinationRecord> vaccinationRecords;
    
    @OneToMany(mappedBy = "nurse")
    private List<VaccinationRecord> nurseVaccinationRecords;
    
    @OneToMany(mappedBy = "student")
    private List<HealthCheckRecord> healthCheckRecords;
    
    @OneToMany(mappedBy = "nurse")
    private List<HealthCheckRecord> nurseHealthCheckRecords;
    
    @OneToMany(mappedBy = "student")
    private List<MedicationSent> medicationSents;
    
    @OneToMany(mappedBy = "student")
    private List<StudentParent> studentParents;
    
    @OneToMany(mappedBy = "parent")
    private List<StudentParent> parentStudents;
    
    @OneToMany(mappedBy = "student")
    private List<HealthCheckConfirmation> healthCheckConfirmations;
    
    @OneToMany(mappedBy = "parent")
    private List<HealthCheckConfirmation> parentHealthCheckConfirmations;
    
    @OneToMany(mappedBy = "student")
    private List<VaccinationConfirmation> vaccinationConfirmations;
    
    @OneToMany(mappedBy = "parent")
    private List<VaccinationConfirmation> parentVaccinationConfirmations;
    
    @Column(name = "email_notifications_enabled")
    private Boolean emailNotificationsEnabled = true;

    @Column(name = "notification_types")
    private String notificationTypes = "FOLLOW_UP,EMERGENCY";
    
    public boolean isLocked() {
        return locked;
    }
} 