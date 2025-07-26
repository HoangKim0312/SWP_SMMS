package com.example.swp_smms.controller;

import com.example.swp_smms.model.entity.MedicalProfileSnapshot;
import com.example.swp_smms.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
