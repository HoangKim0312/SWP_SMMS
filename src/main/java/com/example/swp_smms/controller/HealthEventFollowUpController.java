package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.HealthEventFollowUpRequest;
import com.example.swp_smms.model.payload.response.HealthEventFollowUpResponse;
import com.example.swp_smms.service.HealthEventFollowUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/health-event-followups")
@RequiredArgsConstructor
public class HealthEventFollowUpController {
    private final HealthEventFollowUpService followUpService;

    @PostMapping
    public Object createFollowUp(@RequestBody HealthEventFollowUpRequest request) {
        HealthEventFollowUpResponse response = followUpService.createFollowUp(request);
        return response;
    }

    @GetMapping("/event/{eventId}")
    public Object getFollowUpsByEvent(@PathVariable Long eventId) {
        List<HealthEventFollowUpResponse> response = followUpService.getFollowUpsByEvent(eventId);
        return response;
    }

    @GetMapping("/parent/{parentId}")
    public Object getFollowUpsByParent(@PathVariable UUID parentId) {
        List<HealthEventFollowUpResponse> response = followUpService.getFollowUpsByParent(parentId);
        return response;
    }

    @PutMapping("/{followUpId}/status")
    public Object updateFollowUpStatus(@PathVariable Long followUpId, @RequestParam String status) {
        HealthEventFollowUpResponse response = followUpService.updateFollowUpStatus(followUpId, status);
        return response;
    }

    @PutMapping("/{followUpId}/acknowledge")
    public Object acknowledgeFollowUp(@PathVariable Long followUpId) {
        HealthEventFollowUpResponse response = followUpService.acknowledgeFollowUp(followUpId);
        return response;
    }
} 