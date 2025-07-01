package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.HealthEventRequest;
import com.example.swp_smms.model.payload.response.HealthEventResponse;
import com.example.swp_smms.service.HealthEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/healthEvents")
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

    @GetMapping("/all")
    public ResponseEntity<Object> getAllHealthEvents() {
        List<HealthEventResponse> events = healthEventService.viewAllHealthEvents();
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "All health events retrieved successfully",
                events
        );
    }

    @GetMapping("/by-date/{eventDate}")
    public ResponseEntity<Object> getHealthEventsByDate(@PathVariable String eventDate) {
        List<HealthEventResponse> events = healthEventService.viewHealthEventsByDate(eventDate);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Health events for date retrieved successfully",
                events
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
}