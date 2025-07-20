package com.example.swp_smms.controller;

import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.StudentAllergy;
import com.example.swp_smms.model.entity.StudentCondition;
import com.example.swp_smms.model.entity.StudentDisease;
import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.*;
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

    @GetMapping("/{studentId}/get-medical-profile")
    public ResponseEntity<?> getFullMedicalProfile(@PathVariable String studentId) {
        try {
            return ResponseEntity.ok(medicalProfileService.getFullMedicalProfile(studentId));
        } catch (RuntimeException ex) {
            return ResponseBuilder.error(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return ResponseBuilder.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


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

    @PostMapping("/add-allergy-to-profile")
    public ResponseEntity<?> addAllergyToStudentMedicalProfile(@RequestBody AddAllergyToMedicalProfileRequest request) {
        try {
            StudentAllergy allergy = medicalProfileService.addAllergyToStudentProfile(request);
            return ResponseEntity.ok(allergy);
        } catch (RuntimeException ex) {
            return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return ResponseBuilder.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/add-disease-to-profile")
    public ResponseEntity<?> addStudentDisease(@RequestBody AddStudentDiseaseRequest request) {
        try {
            StudentDisease saved = medicalProfileService.addStudentDisease(request);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException ex) {
            return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return ResponseBuilder.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-condition-to-profile")
    public ResponseEntity<?> addStudentCondition(@RequestBody AddStudentConditionRequest request) {
        try {
            StudentCondition saved = medicalProfileService.addStudentCondition(request);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException ex) {
            return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return ResponseBuilder.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
