package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.MedicationSentResponse;
import com.example.swp_smms.service.MedicationSentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/medication-sent")
@RequiredArgsConstructor
public class MedicationSentController {

    private final MedicationSentService medicationSentService;

    @PostMapping("/create/{studentId}/{parentId}/{sentAt}")
    public ResponseEntity<MedicationSentResponse> createMedicationSent(
            @PathVariable UUID studentId,
            @PathVariable UUID parentId,
            @PathVariable String sentAt,
            @RequestBody MedicationSentRequest request) {

        try {
            MedicationSentResponse response = medicationSentService.createMedicationSent(studentId, parentId, sentAt , request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // or custom error response
        }
    }
}
