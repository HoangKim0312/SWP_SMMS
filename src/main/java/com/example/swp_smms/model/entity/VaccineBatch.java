package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String stockInDate;
    
    @Column(name = "expiry_date")
    private String expiryDate;

    
    @ManyToOne
    @JoinColumn(name = "vaccine_id", insertable = false, updatable = false)
    private Vaccine vaccine;
} 