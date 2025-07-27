package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.enums.HealthEventPriority;

import java.util.UUID;

public interface HealthEventNotificationService {
    
    void notifyParent(HealthEvent healthEvent, UUID parentId);
    void sendEmergencyNotification(HealthEvent healthEvent, UUID parentId);
    void sendApprovalReminder(HealthEvent healthEvent, UUID parentId);
    void sendApprovalConfirmation(HealthEvent healthEvent, UUID parentId);
    boolean shouldSendNotification(HealthEventPriority priority);
} 