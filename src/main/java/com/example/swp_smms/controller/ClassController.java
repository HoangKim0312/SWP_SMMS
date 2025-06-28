package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.ClassRequest;
import com.example.swp_smms.model.payload.response.ClassResponse;
import com.example.swp_smms.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
