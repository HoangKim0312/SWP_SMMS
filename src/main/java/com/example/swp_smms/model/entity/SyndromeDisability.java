package com.example.swp_smms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SyndromeDisability")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SyndromeDisability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Long conditionId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @JsonIgnore
    private ConditionType type;

    /**
     * Priority from 1 (low concern) to 10 (critical concern)
     */
    @Column(name = "priority", nullable = false)
    @JsonIgnore
    private int priority;

    @Column(name = "description", columnDefinition = "TEXT")
    @JsonIgnore
    private String description;

    public enum ConditionType {
        MENTAL,
        PHYSICAL,
        COGNITIVE,
        SENSORY,
        DEVELOPMENTAL,
        OTHER
    }
}
