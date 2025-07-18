package com.example.swp_smms.service;


import com.example.swp_smms.model.entity.SyndromeDisability;
import com.example.swp_smms.model.payload.request.SyndromeDisabilityRequest;

public interface SyndromeDisabilityService {
    SyndromeDisability create(SyndromeDisabilityRequest request);
}