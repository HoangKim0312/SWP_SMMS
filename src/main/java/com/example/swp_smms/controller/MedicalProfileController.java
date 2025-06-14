package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.AccountRequest;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.service.MedicalProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create/{studentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createMedicalProfile(
            @PathVariable UUID studentId,
            @RequestBody MedicalProfileRequest medicalProfileRequest) {

        MedicalProfileResponse createdProfile = medicalProfileService.createMedicalProfile(studentId, medicalProfileRequest);

        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.CREATED,
                "Medical profile created successfully",
                createdProfile
        );
    }


}
