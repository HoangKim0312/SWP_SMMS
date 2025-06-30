package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.entity.HealthEventFollowUp;
import com.example.swp_smms.model.payload.request.HealthEventFollowUpRequest;
import com.example.swp_smms.model.payload.response.HealthEventFollowUpResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.HealthEventFollowUpRepository;
import com.example.swp_smms.repository.HealthEventRepository;
import com.example.swp_smms.service.HealthEventFollowUpService;
import com.example.swp_smms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthEventFollowUpServiceImpl implements HealthEventFollowUpService {
    private final HealthEventFollowUpRepository followUpRepository;
    private final HealthEventRepository healthEventRepository;
    private final AccountRepository accountRepository;
    @Autowired(required = false)
    private EmailService emailService;

    @Override
    @Transactional
    public HealthEventFollowUpResponse createFollowUp(HealthEventFollowUpRequest request) {
        HealthEvent event = healthEventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Health event not found"));
        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        HealthEventFollowUp followUp = new HealthEventFollowUp();
        followUp.setHealthEvent(event);
        followUp.setParent(parent);
        followUp.setInstruction(request.getInstruction());
        followUp.setRequiresDoctor(request.getRequiresDoctor());
        followUp.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
        followUp = followUpRepository.save(followUp);

        // Email notification logic
        if (Boolean.TRUE.equals(parent.getEmailNotificationsEnabled()) &&
            parent.getNotificationTypes() != null &&
            parent.getNotificationTypes().contains("FOLLOW_UP") &&
            emailService != null) {
            String subject = "Follow-Up Instructions for Your Child";
            String body = String.format(
                "Dear Parent,\n\nA follow-up has been created for your child regarding a recent medical incident at school.\n\nInstructions: %s\n\nPlease log in to your parent portal to acknowledge receipt and view more details.\n\nThank you,\nSchool Health Office",
                request.getInstruction()
            );
            try {
                emailService.sendSimpleMessage(parent.getEmail(), subject, body);
            } catch (Exception e) {
                // Log or handle email failure
            }
        }

        return mapToResponse(followUp);
    }

    @Override
    public List<HealthEventFollowUpResponse> getFollowUpsByEvent(Long eventId) {
        return followUpRepository.findByHealthEvent_EventId(eventId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthEventFollowUpResponse> getFollowUpsByParent(UUID parentId) {
        return followUpRepository.findByParent_AccountId(parentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HealthEventFollowUpResponse updateFollowUpStatus(Long followUpId, String status) {
        HealthEventFollowUp followUp = followUpRepository.findById(followUpId)
                .orElseThrow(() -> new RuntimeException("Follow-up not found"));
        followUp.setStatus(status);
        followUp = followUpRepository.save(followUp);
        return mapToResponse(followUp);
    }

    @Override
    @Transactional
    public HealthEventFollowUpResponse acknowledgeFollowUp(Long followUpId) {
        HealthEventFollowUp followUp = followUpRepository.findById(followUpId)
                .orElseThrow(() -> new RuntimeException("Follow-up not found"));
        followUp.setStatus("ACKNOWLEDGED");
        followUp = followUpRepository.save(followUp);
        return mapToResponse(followUp);
    }

    private HealthEventFollowUpResponse mapToResponse(HealthEventFollowUp followUp) {
        HealthEventFollowUpResponse response = new HealthEventFollowUpResponse();
        response.setFollowId(followUp.getFollowId());
        response.setEventId(followUp.getHealthEvent().getEventId());
        response.setParentId(followUp.getParent().getAccountId());
        response.setInstruction(followUp.getInstruction());
        response.setRequiresDoctor(followUp.getRequiresDoctor());
        response.setStatus(followUp.getStatus());
        return response;
    }
} 