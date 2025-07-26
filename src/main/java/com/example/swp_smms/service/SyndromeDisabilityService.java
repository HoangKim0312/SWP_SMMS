package com.example.swp_smms.service;


import com.example.swp_smms.model.entity.SyndromeDisability;
import com.example.swp_smms.model.payload.request.SyndromeDisabilityRequest;
import com.example.swp_smms.model.payload.response.ConditionResponse;

import java.util.List;

public interface SyndromeDisabilityService {
    SyndromeDisability create(SyndromeDisabilityRequest request);
    List<ConditionResponse> getAll();
    List<ConditionResponse> searchByName(String name);

}