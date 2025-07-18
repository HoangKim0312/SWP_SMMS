package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Disease;
import com.example.swp_smms.model.payload.request.DiseaseRequest;

public interface DiseaseService {
    Disease create(DiseaseRequest request);
}
