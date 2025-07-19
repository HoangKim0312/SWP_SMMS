package com.example.swp_smms.snapshotdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAllergySnapshotDTO {
    private Long allergenId;
    private int severity;
    private String reaction;
    private boolean lifeThreatening;
}
