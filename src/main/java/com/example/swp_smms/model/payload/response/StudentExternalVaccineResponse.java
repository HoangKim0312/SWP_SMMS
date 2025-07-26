package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class StudentExternalVaccineResponse {
    private Long externalVaccineId;
    private UUID studentId;
    private UUID submittedBy;
    private Long vaccineId;
    private LocalDate injectionDate;
    private String location;
    private String note;
    private boolean verified;
}
