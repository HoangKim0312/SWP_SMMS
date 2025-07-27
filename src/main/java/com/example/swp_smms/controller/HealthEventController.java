package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.HealthEventRequest;
import com.example.swp_smms.model.payload.request.HealthEventApprovalRequest;
import com.example.swp_smms.model.payload.request.HealthEventSearchRequest;
import com.example.swp_smms.model.payload.response.HealthEventResponse;
import com.example.swp_smms.model.payload.response.HealthEventMedicationResponse;
import com.example.swp_smms.model.payload.response.HealthEventApprovalResponse;
import com.example.swp_smms.service.HealthEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/healthEvents")
public class HealthEventController {

    @Autowired
    private HealthEventService healthEventService;

    @PostMapping("/create/{studentId}/{nurseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createHealthEvent(
            @PathVariable UUID studentId,
            @PathVariable UUID nurseId,
            @RequestBody HealthEventRequest healthEventRequest) {

        HealthEventResponse createdEvent = healthEventService.createHealthEvent(studentId, nurseId, healthEventRequest);

        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.CREATED,
                "Health event created successfully",
                createdEvent
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllHealthEvents() {
        List<HealthEventResponse> events = healthEventService.viewAllHealthEvents();
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "All health events retrieved successfully",
                events
        );
    }

    @GetMapping("/get-by-date/{eventDate}")
    public ResponseEntity<Object> getHealthEventsByDate(@PathVariable String eventDate) {
        List<HealthEventResponse> events = healthEventService.viewHealthEventsByDate(eventDate);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Health events for date retrieved successfully",
                events
        );
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<Object> getHealthEventsByPriority(@PathVariable String priority) {
        List<HealthEventResponse> events = healthEventService.getHealthEventsByPriority(priority);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Health events by priority retrieved successfully",
                events
        );
    }

    @GetMapping("/parent/{parentId}/pending-approval")
    public ResponseEntity<Object> getHealthEventsPendingApproval(@PathVariable UUID parentId) {
        List<HealthEventResponse> events = healthEventService.getHealthEventsPendingApproval(parentId);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Pending approval health events retrieved successfully",
                events
        );
    }

    @PutMapping("/approve")
    public ResponseEntity<Object> approveHealthEvent(@RequestBody HealthEventApprovalRequest request) {
        HealthEventApprovalResponse response = healthEventService.approveHealthEvent(request);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Health event approval processed successfully",
                response
        );
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Object> updateHealthEvent(
            @PathVariable Long eventId,
            @RequestBody HealthEventRequest healthEventRequest) {
        HealthEventResponse updatedEvent = healthEventService.updateHealthEvent(eventId, healthEventRequest);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Health event updated successfully",
                updatedEvent
        );
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Object> deleteHealthEvent(@PathVariable Long eventId) {
        healthEventService.deleteHealthEvent(eventId);
        return ResponseBuilder.responseBuilder(
                HttpStatus.OK,
                "Health event deleted successfully"
        );
    }
    
    @GetMapping("/{eventId}/medications")
    public ResponseEntity<Object> getMedicationsByHealthEvent(@PathVariable Long eventId) {
        List<HealthEventMedicationResponse> medications = healthEventService.getMedicationsByHealthEvent(eventId);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Medications for health event retrieved successfully",
                medications
        );
    }

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<Object> getHealthEventsByNurseId(@PathVariable UUID nurseId) {
        List<HealthEventResponse> events = healthEventService.getHealthEventsByNurseId(nurseId);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Health events for nurse retrieved successfully",
                events
        );
    }

    @PostMapping("/search")
    public ResponseEntity<Object> searchHealthEvents(@RequestBody HealthEventSearchRequest searchRequest) {
        List<HealthEventResponse> events = healthEventService.searchHealthEvents(searchRequest);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Health events search completed successfully",
                events
        );
    }
}