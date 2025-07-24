package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.StudentExternalVaccineRequest;
import com.example.swp_smms.model.entity.StudentExternalVaccine;
import com.example.swp_smms.model.payload.response.StudentExternalVaccineResponse;
import com.example.swp_smms.service.StudentExternalVaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/external-vaccine-records")
public class StudentExternalVaccineController {

    @Autowired
    private StudentExternalVaccineService externalVaccineService;

    @PostMapping("/create")
    public ResponseEntity<StudentExternalVaccineResponse> create(@RequestBody StudentExternalVaccineRequest request) {
        StudentExternalVaccineResponse vaccine = externalVaccineService.create(request);
        return ResponseEntity.ok(vaccine);
    }
    @PutMapping("/{id}/verify")
    public ResponseEntity<StudentExternalVaccineResponse> verifyExternalVaccine(@PathVariable Long id) {
        StudentExternalVaccineResponse response = externalVaccineService.verify(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/student/{studentId}/verified")
    public ResponseEntity<List<StudentExternalVaccineResponse>> getVerifiedExternalVaccinesByStudent(
            @PathVariable UUID studentId,
            @RequestParam boolean status) {
        List<StudentExternalVaccineResponse> vaccines = externalVaccineService
                .getExternalVaccinesByStudentAndVerifiedStatus(studentId, status);
        return ResponseEntity.ok(vaccines);
    }

}
