package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Allergen;
import com.example.swp_smms.model.payload.request.AllergenRequest;

public interface AllergenService {
    Allergen create(AllergenRequest request);
}
