package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Allergen;
import com.example.swp_smms.model.payload.request.AllergenRequest;

import java.util.List;

public interface AllergenService {
    Allergen create(AllergenRequest request);
    List<Allergen> searchByName(String name);
    List<Allergen> getAll();
}
