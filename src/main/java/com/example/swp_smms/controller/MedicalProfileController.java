package com.example.swp_smms.controller;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;
import com.example.swp_smms.service.MedicalProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicalProfiles")
public class MedicalProfileController {

    @Autowired
    private MedicalProfileService medicalProfileService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> createMedicalProfile(@RequestBody MedicalProfileRequest request) {
        try {
            MedicalProfile profile = medicalProfileService.createMedicalProfile(request);
            MedicalProfileResponse response = modelMapper.map(profile, MedicalProfileResponse.class);
            return ResponseBuilder.success(response); // Custom success response
        } catch (RuntimeException ex) {
            return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return ResponseBuilder.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
