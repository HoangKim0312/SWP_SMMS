package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.MedicationRequest;
import com.example.swp_smms.model.payload.response.MedicationResponse;
import com.example.swp_smms.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public Object createMedication(@Valid @RequestBody MedicationRequest request) {
        MedicationResponse response = medicationService.createMedication(request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Medication created successfully", response);
    }

    @GetMapping("/{id}")
    public Object getMedicationById(@PathVariable Long id) {
        MedicationResponse response = medicationService.getMedicationById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Medication fetched successfully", response);
    }

    @GetMapping
    public Object getAllMedications() {
        List<MedicationResponse> response = medicationService.getAllMedications();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All medications fetched successfully", response);
    }

    @GetMapping("/search")
    public Object searchMedications(@RequestParam String name) {
        List<MedicationResponse> response = medicationService.searchMedicationsByName(name);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Medications fetched successfully", response);
    }

    @GetMapping("/low-stock")
    public Object getLowStockMedications(@RequestParam(defaultValue = "10") Integer threshold) {
        List<MedicationResponse> response = medicationService.getLowStockMedications(threshold);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Low stock medications fetched successfully", response);
    }

    @PutMapping("/{id}")
    public Object updateMedication(@PathVariable Long id, @Valid @RequestBody MedicationRequest request) {
        MedicationResponse response = medicationService.updateMedication(id, request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Medication updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public Object deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Medication deleted successfully");
    }
} 