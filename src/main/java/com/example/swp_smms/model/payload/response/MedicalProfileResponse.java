package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllergyDTO {
        private Long studentAllergyId;
        private Long allergenId;
        private String allergenName;
        private String reaction;
        private int severity;
        private boolean isLifeThreatening;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiseaseDTO {
        private Long studentDiseaseId;
        private Long diseaseId;
        private String diseaseName;
        private String sinceDate;
        private int severity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConditionDTO {
        private Long studentConditionId;
        private Long conditionId;
        private String conditionName;
        private String note;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicHealthDataDTO {
        private Long studentBasicHealthId;
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
