package com.example.swp_smms.model.payload.request;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentExternalVaccineRequest {
    private UUID studentId;
    private UUID submittedBy;
    private Long vaccineId;
    private LocalDate injectionDate;
    private String location;
    private String note;
}
