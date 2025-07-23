package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Disease;
import com.example.swp_smms.model.payload.request.DiseaseRequest;
import com.example.swp_smms.model.payload.response.DiseaseResponse;

import java.util.List;

public interface DiseaseService {
    Disease create(DiseaseRequest request);
    List<DiseaseResponse> getAll();
    List<DiseaseResponse> searchByName(String name);
}
