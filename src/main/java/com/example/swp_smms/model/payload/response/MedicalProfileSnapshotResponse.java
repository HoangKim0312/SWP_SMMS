package com.example.swp_smms.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalProfileSnapshotResponse {
    private Long id;
    private Long medicalProfileId;
    private LocalDateTime snapshotTime;
    private SnapshotMedicalProfileDTO medicalProfileData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SnapshotMedicalProfileDTO {
        private List<AllergySnapshotDTO> allergies;
        private List<DiseaseSnapshotDTO> diseases;
        private List<ConditionSnapshotDTO> conditions;
        private BasicHealthDataDTO basicHealthData;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class AllergySnapshotDTO {
            private Long studentAllergyId;
            private AllergenDTO allergen;
            private String reaction;
            private int severity;
            private boolean lifeThreatening;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class AllergenDTO {
                private Long allergenId;
                private String name;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class DiseaseSnapshotDTO {
            private Long id;
            private DiseaseDTO disease;
            private String sinceDate;
            private int severity;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class DiseaseDTO {
                private Long diseaseId;
                private String name;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ConditionSnapshotDTO {
            private Long id;
            private ConditionDTO syndromeDisability;
            private String note;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class ConditionDTO {
                private Long conditionId;
                private String name;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class BasicHealthDataDTO {
            private Long id;
            private double heightCm;
            private double weightKg;
            private String visionLeft;
            private String visionRight;
            private String hearingStatus;
            private String gender;
            private String bloodType;
            private LocalDate lastMeasured;
        }
    }

}
