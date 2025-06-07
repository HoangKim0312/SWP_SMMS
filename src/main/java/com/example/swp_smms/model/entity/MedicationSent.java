package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "medication_name")
    private String medicationName;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "frequency_per_day")
    private Integer frequencyPerDay;

    @Column(name = "timing_notes")
    private String timingNotes;

    @Column(name = "sent_at")
    private String sentAt;

    @Column(name = "amount")
    private Integer amount;
} 