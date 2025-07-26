package com.example.swp_smms.controller;

import com.example.swp_smms.model.entity.MedicalProfileSnapshot;
import com.example.swp_smms.model.payload.response.MedicalProfileSnapshotResponse;
import com.example.swp_smms.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@RestController
@RequestMapping("/api/snapshots")
public class SnapshotController {

    @Autowired
    private SnapshotService snapshotService;

    @PostMapping("/{medicalProfileId}")
    public ResponseEntity<?> createSnapshot(@PathVariable Long medicalProfileId) {
        try {
            MedicalProfileSnapshot snapshot = snapshotService.createSnapshot(medicalProfileId);
            return ResponseEntity.ok(snapshot);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create snapshot: " + e.getMessage());
        }
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getAllSnapshotsByStudentId(
            @PathVariable UUID studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "snapshotTime"));
            Page<MedicalProfileSnapshotResponse> snapshotPage = snapshotService.getSnapshotsByStudentId(studentId, pageable);
            return ResponseEntity.ok(snapshotPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching snapshots: " + e.getMessage());
        }
    }

}
