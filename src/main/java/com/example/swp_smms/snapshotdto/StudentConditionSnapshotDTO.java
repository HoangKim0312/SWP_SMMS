package com.example.swp_smms.snapshotdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentConditionSnapshotDTO {
    private Long syndromeDisabilityId;
    private String note;
}