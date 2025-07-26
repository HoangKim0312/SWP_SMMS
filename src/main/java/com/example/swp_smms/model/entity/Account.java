package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "account")
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

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private MedicalProfile medicalProfile;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<HealthEvent> healthEvents;
    
    @OneToMany(mappedBy = "nurse")
    @JsonIgnore
    private List<HealthEvent> nurseHealthEvents;
    
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<VaccinationRecord> vaccinationRecords;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<StudentExternalVaccine> studentExternalVaccines;


    @OneToMany(mappedBy = "nurse")
    @JsonIgnore
    private List<VaccinationRecord> nurseVaccinationRecords;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<HealthCheckRecord> healthCheckRecords;
    
    @OneToMany(mappedBy = "nurse")
    @JsonIgnore
    private List<HealthCheckRecord> nurseHealthCheckRecords;
    
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<MedicationSent> medicationSents;
    
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<StudentParent> studentParents;
    
    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<StudentParent> parentStudents;
    
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<HealthCheckConfirmation> healthCheckConfirmations;
    
    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<HealthCheckConfirmation> parentHealthCheckConfirmations;
    
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<VaccinationConfirmation> vaccinationConfirmations;
    
    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<VaccinationConfirmation> parentVaccinationConfirmations;


    @Column(name = "email_notifications_enabled")
    @JsonIgnore
    private Boolean emailNotificationsEnabled = true;

    @Column(name = "notification_types")
    @JsonIgnore
    private String notificationTypes = "FOLLOW_UP,EMERGENCY";
    
    public boolean isLocked() {
        return locked;
    }
} 