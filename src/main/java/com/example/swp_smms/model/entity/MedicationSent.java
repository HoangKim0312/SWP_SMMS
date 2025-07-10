package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MedicationSent")
public class MedicationSent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "med_sent_id")
    private Long medSentId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "account_id")
    private Account parent;

    @Column(name = "request_date") // Only today or tomorrow
    private LocalDate requestDate;

    @Column(name = "sent_at") // When the parent submitted the request
    private LocalDate sentAt;

    @Column(name = "is_active")
    private boolean isActive = true; // Default to true

    @Column(name = "is_accepted")
    private Boolean isAccepted = null; // Default to null

    @OneToMany(mappedBy = "medicationSent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dosage> dosages;

}
