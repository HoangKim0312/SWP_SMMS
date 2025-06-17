package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.EventMedicationUsageRequest;
import com.example.swp_smms.model.payload.response.EventMedicationUsageResponse;
import com.example.swp_smms.service.EventMedicationUsageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-medication-usage")
@RequiredArgsConstructor
public class EventMedicationUsageController {

    private final EventMedicationUsageService usageService;

    @PostMapping
    public Object createUsage(@Valid @RequestBody EventMedicationUsageRequest request) {
        EventMedicationUsageResponse response = usageService.createUsage(request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Event medication usage created successfully", response);
    }

    @GetMapping("/{id}")
    public Object getUsageById(@PathVariable Long id) {
        EventMedicationUsageResponse response = usageService.getUsageById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Event medication usage fetched successfully", response);
    }

    @GetMapping
    public Object getAllUsages() {
        List<EventMedicationUsageResponse> response = usageService.getAllUsages();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All event medication usages fetched successfully", response);
    }

    @GetMapping("/health-event/{eventId}")
    public Object getUsagesByHealthEvent(@PathVariable Long eventId) {
        List<EventMedicationUsageResponse> response = usageService.getUsagesByHealthEvent(eventId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Event medication usages by health event fetched successfully", response);
    }

    @GetMapping("/medication/{medicationId}")
    public Object getUsagesByMedication(@PathVariable Long medicationId) {
        List<EventMedicationUsageResponse> response = usageService.getUsagesByMedication(medicationId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Event medication usages by medication fetched successfully", response);
    }

    @PutMapping("/{id}")
    public Object updateUsage(@PathVariable Long id, @Valid @RequestBody EventMedicationUsageRequest request) {
        EventMedicationUsageResponse response = usageService.updateUsage(id, request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Event medication usage updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public Object deleteUsage(@PathVariable Long id) {
        usageService.deleteUsage(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Event medication usage deleted successfully");
    }
} 