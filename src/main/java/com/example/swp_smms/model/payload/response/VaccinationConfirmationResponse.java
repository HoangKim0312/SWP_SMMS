package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationConfirmationResponse {
    private Long confirmationId;
    private Long vaccineNoticeId;
    private String vaccineName;
    private UUID studentId;
    private String studentName;
    private UUID parentId;
    private String parentName;
    private String status;
    private String confirmedAt;
} 