package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.AcceptMedicationSentRequest;
import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.ListMedicationSentResponse;
import com.example.swp_smms.model.payload.response.MedicationSentResponse;
import com.example.swp_smms.service.MedicationSentService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/medication-sent")
@RequiredArgsConstructor
public class MedicationSentController {

    private final MedicationSentService medicationSentService;

    @PostMapping("/create/{studentId}/{parentId}")
    public ResponseEntity<?> createMedicationSent(
            @PathVariable UUID studentId,
            @PathVariable UUID parentId,
            @RequestBody MedicationSentRequest request) {
        try {
            MedicationSentResponse response = medicationSentService.createMedicationSent(studentId, parentId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create MedicationSent: " + e.getMessage());
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

    @GetMapping("/all-students/active-med-sents")
    public ResponseEntity<ListMedicationSentResponse> getAllActiveMedicationSentsForAllStudents() {
        ListMedicationSentResponse response = medicationSentService.getAllActiveMedicationSentsForAllStudents();

        if (response.getMedicationSentList().isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(response); // 200 OK
    }

    @GetMapping("/student/{studentId}/med-sent/{medicationSentId}")
    public ResponseEntity<MedicationSentResponse> getMedicationSentById(
            @PathVariable UUID studentId,
            @PathVariable Long medicationSentId) {
        try {
            MedicationSentResponse response = medicationSentService.getMedicationSentById(studentId, medicationSentId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/nurse/accept/{medicationSentId}")
    public ResponseEntity<?> updateMedicationSentAcceptance(
            @PathVariable Long medicationSentId,
            @RequestBody AcceptMedicationSentRequest request) {
        try {
            medicationSentService.updateMedicationSentAcceptance(medicationSentId, request.getIsAccepted());
            return ResponseEntity.ok("MedicationSent has been " +
                    (request.getIsAccepted() ? "accepted" : "rejected") + " successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/accepted")
    public ResponseEntity<ListMedicationSentResponse> getAllAcceptedMedicationSents(
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        ListMedicationSentResponse response = medicationSentService.getAcceptedMedicationSents(studentId, date);

        if (response.getMedicationSentList().isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(response); // 200
    }


    @GetMapping("/declined")
    public ResponseEntity<ListMedicationSentResponse> getDeclinedMedicationSents(
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {

        ListMedicationSentResponse response = medicationSentService.getDeclinedMedicationSents(studentId, requestDate);

        if (response.getMedicationSentList().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }



}
