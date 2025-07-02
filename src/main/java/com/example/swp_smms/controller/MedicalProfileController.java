package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.*;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.service.MedicalProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medicalProfiles")

public class MedicalProfileController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MedicalProfileService medicalProfileService;

    @PostMapping("/create/{studentId}/{recordId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createMedicalProfile(
            @PathVariable UUID studentId,
            @RequestParam(required = false) Long recordId,
            @RequestBody MedicalProfileRequest medicalProfileRequest) {

        MedicalProfileResponse createdProfile = medicalProfileService.createMedicalProfile(studentId,recordId ,medicalProfileRequest);

        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.CREATED,
                "Medical profile created successfully",
                createdProfile
        );
    }

    @GetMapping("/latest/{studentId}")
    public ResponseEntity<MedicalProfileResponse> getLatestMedicalProfile(@PathVariable UUID studentId) {
        MedicalProfileResponse response = medicalProfileService.getLastMedicalProfile(studentId);

        if (response == null) {
            return ResponseEntity.notFound().build(); // 404 if no profile found
        }

        return ResponseEntity.ok(response); // 200 OK with the latest profile
    }
    @GetMapping("/all/{studentId}")
    public ResponseEntity<ListMedicalProfileResponse> getAllMedicalProfiles(@PathVariable UUID studentId) {
        ListMedicalProfileResponse response = medicalProfileService.getAllMedicalProfiles(studentId);

        if (response == null || response.getMedicalProfiles() == null || response.getMedicalProfiles().isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 if no profiles found
        }

        return ResponseEntity.ok(response); // 200 OK with the list of profiles
    }
    
    @DeleteMapping("/delete/{studentId}/{medicalProfileId}")
    public ResponseEntity<Object> deleteMedicalProfile(
            @PathVariable UUID studentId,
            @PathVariable Long medicalProfileId) {

        medicalProfileService.deleteMedicalProfile(studentId, medicalProfileId);

        return ResponseEntity.ok("Medical profile deleted successfully");
    }



}
