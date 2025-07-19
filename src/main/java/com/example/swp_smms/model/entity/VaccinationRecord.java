package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;@Entity
@Table(name = "VaccinationRecord")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "account_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "nurse_id", referencedColumnName = "account_id")
    private Account nurse;

    @ManyToOne
    @JoinColumn(name = "vaccine_notice_id")
    private VaccinationNotice vaccinationNotice;

    @ManyToOne
    @JoinColumn(name = "vaccine_id")
    private Vaccine vaccine;

    @Column(name = "results")
    private String results;

    @Column(name = "reaction")
    private String reaction;

    @Column(name = "dose_number")
    private Integer doseNumber;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "date")
    private LocalDate date;
}
