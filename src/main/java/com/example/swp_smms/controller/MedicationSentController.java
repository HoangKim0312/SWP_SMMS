package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.ListMedicationSentResponse;
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

    @PostMapping("/create/{studentId}/{parentId}")
    public ResponseEntity<MedicationSentResponse> createMedicationSent(
            @PathVariable UUID studentId,
            @PathVariable UUID parentId,
            @RequestBody MedicationSentRequest request) {

        try {
            MedicationSentResponse response = medicationSentService.createMedicationSent(studentId, parentId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // or custom error response
        }
    }


    @GetMapping("/student/{studentId}/active-med-sents")
    public ResponseEntity<ListMedicationSentResponse> getActiveMedicationSentsForStudent(
            @PathVariable UUID studentId) {

        ListMedicationSentResponse response = medicationSentService.getAllActiveMedicationSentsForStudent(studentId);

        if (response.getMedicationSentList().isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if nothing found
        }

        return ResponseEntity.ok(response); // 200 OK with data
    }

    @GetMapping("/student/{studentId}/all-med-sents")
    public ResponseEntity<ListMedicationSentResponse> getAllMedicationSentsForStudent(
            @PathVariable UUID studentId) {

        ListMedicationSentResponse response = medicationSentService.getAllMedicationSentsForStudent(studentId);

        if (response.getMedicationSentList().isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if nothing found
        }

        return ResponseEntity.ok(response); // 200 OK with data
    }

    @DeleteMapping("/delete/{studentId}/{medicationSentId}")
    public ResponseEntity<Object> deleteMedicationSent(
            @PathVariable UUID studentId,
            @PathVariable Long medicationSentId) {

        medicationSentService.deleteMedicationSent(studentId, medicationSentId);

        return ResponseEntity.ok("MedicationSent deleted successfully");
    }

    // Update MedicationSent
    @PutMapping("/update/{studentId}/{medicationSentId}")
    public ResponseEntity<Object> updateMedicationSent(
            @PathVariable UUID studentId,
            @PathVariable Long medicationSentId,
            @RequestBody MedicationSentRequest request) {

        MedicationSentResponse updated = medicationSentService.updateMedicationSent(studentId, medicationSentId, request);

        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "MedicationSent updated successfully",
                updated
        );
    }


}
