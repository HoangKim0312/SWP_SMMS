package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class ListMedicalProfileResponse {
    private List<MedicalProfileResponse> medicalProfiles;
}
