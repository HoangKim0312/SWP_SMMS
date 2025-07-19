package com.example.swp_smms.snapshotdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDiseaseSnapshotDTO {
    private Long diseaseId;
    private int severity;
    private String sinceDate;
}