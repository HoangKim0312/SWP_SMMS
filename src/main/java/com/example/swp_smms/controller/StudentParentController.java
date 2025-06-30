package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.StudentParentLinkRequest;
import com.example.swp_smms.model.payload.response.ResponseBuilder;
import com.example.swp_smms.service.StudentParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student-parents")
@RequiredArgsConstructor
public class StudentParentController {
    private final StudentParentService studentParentService;

    @PostMapping
    public ResponseEntity<?> linkStudentParent(@RequestBody StudentParentLinkRequest request) {
        studentParentService.linkStudentParent(request.getStudentId(), request.getParentId());
        return ResponseEntity.ok(ResponseBuilder.success("Linked student and parent successfully"));
    }

    @DeleteMapping
    public ResponseEntity<?> unlinkStudentParent(@RequestBody StudentParentLinkRequest request) {
        studentParentService.unlinkStudentParent(request.getStudentId(), request.getParentId());
        return ResponseEntity.ok(ResponseBuilder.success("Unlinked student and parent successfully"));
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<?> getChildrenForParent(@PathVariable("parentId") java.util.UUID parentId) {
        var children = studentParentService.getChildrenForParent(parentId);
        return ResponseEntity.ok(children);
    }
} 