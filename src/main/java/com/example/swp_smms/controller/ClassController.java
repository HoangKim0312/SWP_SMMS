package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.ClassRequest;
import com.example.swp_smms.model.payload.response.ClassResponse;
import com.example.swp_smms.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<ClassResponse> createClass(@RequestBody ClassRequest request) {
        ClassResponse created = classService.createClass(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @GetMapping("/by-grade")
    public ResponseEntity<List<ClassResponse>> getClassesByGrade(@RequestParam String grade) {
        List<ClassResponse> responses = classService.getClassesByGradeInCurrentYear(grade);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-student")
    public ResponseEntity<ClassResponse> getClassByStudent(@RequestParam UUID studentId) {
        ClassResponse response = classService.getClassByStudentId(studentId);
        return ResponseEntity.ok(response);
    }



}
