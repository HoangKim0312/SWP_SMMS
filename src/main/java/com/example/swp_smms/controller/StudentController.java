package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.response.StudentMedicalSummaryResponse;
import com.example.swp_smms.service.AccountService;
import com.example.swp_smms.service.MedicalProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final MedicalProfileService medicalProfileService;
    private final AccountService accountService;

    @GetMapping("/students/{studentId}/summary")
    public ResponseEntity<StudentMedicalSummaryResponse> getStudentMedicalSummary(@PathVariable UUID studentId) {
        StudentMedicalSummaryResponse summary = accountService.getStudentMedicalSummary(studentId);
        return ResponseEntity.ok(summary);
    }

}
