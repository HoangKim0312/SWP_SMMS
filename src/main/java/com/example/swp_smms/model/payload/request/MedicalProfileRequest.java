package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class MedicalProfileRequest {
    private UUID studentId;
    private List<AllergyDTO> allergies;
    private List<DiseaseDTO> diseases;
    private List<ConditionDTO> conditions;
    private BasicHealthDataDTO basicHealthData;

    @Data
    public static class AllergyDTO {
        private Long allergenId;
        private String reaction;
        private int severity;
        private boolean isLifeThreatening;
    }

    @Data
    public static class DiseaseDTO {
        private Long diseaseId;
        private String sinceDate;
        private int severity;
    }

    @Data
    public static class ConditionDTO {
        private Long conditionId;
        private String note;
    }

    @Data
    public static class BasicHealthDataDTO {
        private Double heightCm;
        private Double weightKg;
        private String visionLeft;
        private String visionRight;
        private String hearingStatus;
        private String gender;
        private String bloodType;
        private String lastMeasured;
    }

}
