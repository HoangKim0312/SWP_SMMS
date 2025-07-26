package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Disease")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long diseaseId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @JsonIgnore
    private String description;

    /**
     * Severity scale from 1 (least severe) to 10 (most severe)
     */
    @Column(name = "severity_level", nullable = false)
    @JsonIgnore
    private int severityLevel;
    /*
        1–3: Mild (e.g., seasonal allergies)
        4–6: Moderate (e.g., asthma, diabetes)
        7–10: Severe or life-threatening (e.g., cancer, epilepsy)
     */

    @Column(name = "chronic", nullable = false)
    @JsonIgnore
    private boolean chronic;

    @Column(name = "contagious", nullable = false)
    @JsonIgnore
    private boolean contagious;
}
