package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VaccineBatch")
public class VaccineBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_batch_id")
    private Long vaccineBatchId;
    
    @Column(name = "vaccine_id")
    private Long vaccineId;

    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "stock_in_date")
    private LocalDate stockInDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @ManyToOne
    @JoinColumn(name = "vaccine_id", insertable = false, updatable = false)
    private Vaccine vaccine;
} 