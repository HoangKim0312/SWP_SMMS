package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MedicalProfileResponse {
    private Long medicalProfileId;
    private boolean isActive;
    private LocalDateTime lastUpdated;
    private UUID studentId;

    private List<AllergyDTO> allergies;
    private List<DiseaseDTO> diseases;
    private List<ConditionDTO> conditions;
    private BasicHealthDataDTO basicHealthData;

    @Data
    public static class AllergyDTO {
        private Long allergenId;
        private String allergenName;
        private String reaction;
        private int severity;
        private boolean isLifeThreatening;
    }

    @Data
    public static class DiseaseDTO {
        private Long diseaseId;
        private String diseaseName;
        private String sinceDate;
        private int severity;
    }

    @Data
    public static class ConditionDTO {
        private Long conditionId;
        private String conditionName;
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
