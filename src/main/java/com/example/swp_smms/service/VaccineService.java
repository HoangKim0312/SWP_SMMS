package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.VaccineRequest;
import com.example.swp_smms.model.payload.response.VaccineResponse;
import java.util.List;

public interface VaccineService {
    List<VaccineResponse> getAllVaccines();

    VaccineResponse createVaccine(VaccineRequest request);
}
