package com.example.swp_smms.model.payload.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class EventMedicationUsageResponse {
    private Long usageId;
    private Long healthEventId;
    private String healthEventDescription;
    private Long medicationId;
    private String medicationName;
    private Integer quantityUsed;
    private String time;
    private String dosage;
} 