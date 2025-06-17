package com.example.swp_smms.model.payload.response;

import com.example.swp_smms.model.entity.MedicationSent;
import lombok.Data;

import java.util.List;

@Data
public class ListMedicationSentResponse {
    private List<MedicationSentResponse> medicationSentList;
}
