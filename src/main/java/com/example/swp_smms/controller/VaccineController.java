package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.ReduceQuantityRequest;
import com.example.swp_smms.model.payload.request.VaccineBatchRequest;
import com.example.swp_smms.model.payload.request.VaccineRequest;
import com.example.swp_smms.model.payload.response.VaccineBatchResponse;
import com.example.swp_smms.model.payload.response.VaccineResponse;
import com.example.swp_smms.service.VaccineBatchService;
import com.example.swp_smms.service.VaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VaccineController {

    private final VaccineService vaccineService;
    private final VaccineBatchService vaccineBatchService;


    @GetMapping("/all-vaccines")
    public ResponseEntity<List<VaccineResponse>> getAllVaccines() {
        List<VaccineResponse> responseList = vaccineService.getAllVaccines();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/create-vaccine")
    public ResponseEntity<VaccineResponse> createVaccine(@RequestBody VaccineRequest request) {
        VaccineResponse created = vaccineService.createVaccine(request);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{vaccineId}/create-vaccine-batch")
    public ResponseEntity<VaccineBatchResponse> createBatch(
            @PathVariable Long vaccineId,
            @RequestBody VaccineBatchRequest request) {
        VaccineBatchResponse created = vaccineBatchService.createBatch(vaccineId, request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/vaccines/{vaccineId}/batches")
    public ResponseEntity<List<VaccineBatchResponse>> getBatchesByVaccineId(@PathVariable Long vaccineId) {
        List<VaccineBatchResponse> batches = vaccineBatchService.getAllBatchesByVaccineId(vaccineId);
        return ResponseEntity.ok(batches);
    }

    @PatchMapping("/vaccine-batches/{batchId}/reduce")
    public ResponseEntity<String> reduceVaccineBatchQuantity(
            @PathVariable Long batchId,
            @RequestBody ReduceQuantityRequest request) {
        vaccineBatchService.reduceBatchQuantity(batchId, request.getQuantityToReduce());
        return ResponseEntity.ok("Batch quantity updated successfully.");
    }

}
