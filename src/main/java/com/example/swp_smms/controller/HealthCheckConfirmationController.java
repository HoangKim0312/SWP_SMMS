package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.HealthCheckConfirmationRequest;
import com.example.swp_smms.model.payload.response.HealthCheckConfirmationResponse;
import com.example.swp_smms.service.HealthCheckConfirmationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/health-check-confirmations")
@RequiredArgsConstructor
public class HealthCheckConfirmationController {

    private final HealthCheckConfirmationService confirmationService;

    @PostMapping
    public Object createConfirmation(@Valid @RequestBody HealthCheckConfirmationRequest request) {
        HealthCheckConfirmationResponse response = confirmationService.createConfirmation(request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmation created successfully", response);
    }

    @GetMapping("/getByID/{id}")
    public Object getConfirmationById(@PathVariable Long id) {
        HealthCheckConfirmationResponse response = confirmationService.getConfirmationById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmation fetched successfully", response);
    }

    @GetMapping("/all")
    public Object getAllConfirmations() {
        List<HealthCheckConfirmationResponse> response = confirmationService.getAllConfirmations();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All health check confirmations fetched successfully", response);
    }

    @GetMapping("/getByStudent/{studentId}")
    public Object getConfirmationsByStudent(@PathVariable UUID studentId) {
        List<HealthCheckConfirmationResponse> response = confirmationService.getConfirmationsByStudent(studentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmations for student fetched successfully", response);
    }

    @GetMapping("/getByParent/{parentId}")
    public Object getConfirmationsByParent(@PathVariable UUID parentId) {
        List<HealthCheckConfirmationResponse> response = confirmationService.getConfirmationsByParent(parentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmations for parent fetched successfully", response);
    }

    @GetMapping("/getByNotice/{checkNoticeId}")
    public Object getConfirmationsByNotice(@PathVariable Long checkNoticeId) {
        List<HealthCheckConfirmationResponse> response = confirmationService.getConfirmationsByNotice(checkNoticeId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmations for notice fetched successfully", response);
    }

    @GetMapping("/getByStatus/{status}")
    public Object getConfirmationsByStatus(@PathVariable String status) {
        List<HealthCheckConfirmationResponse> response = confirmationService.getConfirmationsByStatus(status);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmations by status fetched successfully", response);
    }

    @GetMapping("/getByDate/{confirmedAt}")
    public Object getConfirmationsByDate(@PathVariable String confirmedAt) {
        LocalDateTime parsedDateTime = LocalDateTime.parse(confirmedAt);
        List<HealthCheckConfirmationResponse> response = confirmationService.getConfirmationsByDate(parsedDateTime);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmations by date fetched successfully", response);
    }

    @GetMapping("/getByTitle/{title}")
    public Object getConfirmationsByTitle(@PathVariable String title) {
        List<HealthCheckConfirmationResponse> response = confirmationService.getConfirmationsByTitle(title);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmations by title fetched successfully", response);
    }

    @PutMapping("/update/{id}")
    public Object updateConfirmation(@PathVariable Long id, @Valid @RequestBody HealthCheckConfirmationRequest request) {
        HealthCheckConfirmationResponse response = confirmationService.updateConfirmation(id, request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check confirmation updated successfully", response);
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteConfirmation(@PathVariable Long id) {
        confirmationService.deleteConfirmation(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Health check confirmation deleted successfully");
    }
} 