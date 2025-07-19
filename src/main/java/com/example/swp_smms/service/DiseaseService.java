package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Disease;
import com.example.swp_smms.model.payload.request.DiseaseRequest;

import java.util.List;

public interface DiseaseService {
    Disease create(DiseaseRequest request);
    List<Disease> getAll();
    List<Disease> searchByName(String name);

}
