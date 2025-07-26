package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.enums.HealthEventPriority;

import java.util.UUID;

public interface HealthEventNotificationService {
    
    /**
     * Send notification to parent based on health event priority
     * @param healthEvent The health event that occurred
     * @param parentId The parent's account ID
     */
    void notifyParent(HealthEvent healthEvent, UUID parentId);
    
    /**
     * Send emergency notification for critical health events
     * @param healthEvent The critical health event
     * @param parentId The parent's account ID
     */
    void sendEmergencyNotification(HealthEvent healthEvent, UUID parentId);
    
    /**
     * Send approval reminder to parent
     * @param healthEvent The health event waiting for approval
     * @param parentId The parent's account ID
     */
    void sendApprovalReminder(HealthEvent healthEvent, UUID parentId);
    
    /**
     * Send approval confirmation to parent
     * @param healthEvent The approved health event
     * @param parentId The parent's account ID
     */
    void sendApprovalConfirmation(HealthEvent healthEvent, UUID parentId);
    
    /**
     * Check if notification should be sent based on priority
     * @param priority The health event priority
     * @return true if notification should be sent
     */
    boolean shouldSendNotification(HealthEventPriority priority);
} 