package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Allergen")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Allergen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergen_id")
    private Long allergenId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
