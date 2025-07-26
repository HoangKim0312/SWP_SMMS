package com.example.swp_smms.controller;

import com.example.swp_smms.model.entity.Allergen;
import com.example.swp_smms.model.entity.Disease;
import com.example.swp_smms.model.entity.SyndromeDisability;
import com.example.swp_smms.model.payload.request.AllergenRequest;
import com.example.swp_smms.model.payload.request.DiseaseRequest;
import com.example.swp_smms.model.payload.request.SyndromeDisabilityRequest;
import com.example.swp_smms.model.payload.response.DiseaseResponse;
import com.example.swp_smms.service.AllergenService;
import com.example.swp_smms.service.DiseaseService;
import com.example.swp_smms.service.SyndromeDisabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
public class MedicalReferenceController {

    private final DiseaseService diseaseService;
    private final AllergenService allergenService;
    private final SyndromeDisabilityService syndromeService;

    @PostMapping("/diseases")
    public ResponseEntity<Disease> createDisease(@RequestBody DiseaseRequest request) {
        return ResponseEntity.ok(diseaseService.create(request));
    }

    @PostMapping("/allergens")
    public ResponseEntity<Allergen> createAllergen(@RequestBody AllergenRequest request) {
        return ResponseEntity.ok(allergenService.create(request));
    }

    @PostMapping("/syndromes")
    public ResponseEntity<SyndromeDisability> createSyndrome(@RequestBody SyndromeDisabilityRequest request) {
        return ResponseEntity.ok(syndromeService.create(request));
    }

    @GetMapping("/allergens/search")
    public ResponseEntity<?> searchAllergens(@RequestParam(required = false, defaultValue = "") String name) {
        if (name.isBlank()) {
            return ResponseEntity.ok(allergenService.getAll());
        } else {
            return ResponseEntity.ok(allergenService.searchByName(name));
        }
    }

    @GetMapping("/diseases/search")
    public ResponseEntity<List<DiseaseResponse>> searchDiseases(
            @RequestParam(required = false, defaultValue = "") String name) {
        if (name.isBlank()) {
            return ResponseEntity.ok(diseaseService.getAll());
        } else {
            return ResponseEntity.ok(diseaseService.searchByName(name));
        }
    }

    @GetMapping("/syndromes/search")
    public ResponseEntity<?> searchSyndromes(@RequestParam(required = false, defaultValue = "") String name) {
        if (name.isBlank()) {
            return ResponseEntity.ok(syndromeService.getAll());
        } else {
            return ResponseEntity.ok(syndromeService.searchByName(name));
        }
    }


}
