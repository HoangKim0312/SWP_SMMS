package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import com.example.swp_smms.model.enums.HealthEventPriority;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.service.EmailService;
import com.example.swp_smms.service.HealthEventNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthEventNotificationServiceImpl implements HealthEventNotificationService {

    private final EmailService emailService;
    private final AccountRepository accountRepository;
    
    private static final String SYSTEM_BASE_URL = "http://localhost:8080"; // Configure based on your environment

    @Override
    public void notifyParent(HealthEvent healthEvent, UUID parentId) {
        try {
            Optional<Account> parentOpt = accountRepository.findById(parentId);
            if (parentOpt.isEmpty()) {
                log.error("Parent not found with ID: {}", parentId);
                return;
            }
            
            Account parent = parentOpt.get();
            String subject = buildNotificationSubject(healthEvent);
            String content = buildNotificationContent(healthEvent, parent);
            
            emailService.sendSimpleMessage(parent.getEmail(), subject, content);
            log.info("Notification sent to parent {} for medical incident {}", parent.getEmail(), healthEvent.getEventId());
            
        } catch (Exception e) {
            log.error("Failed to send notification to parent {} for medical incident {}", parentId, healthEvent.getEventId(), e);
        }
    }

    @Override
    public void sendEmergencyNotification(HealthEvent healthEvent, UUID parentId) {
        try {
            Optional<Account> parentOpt = accountRepository.findById(parentId);
            if (parentOpt.isEmpty()) {
                log.error("Parent not found with ID: {}", parentId);
                return;
            }
            
            Account parent = parentOpt.get();
            String subject = "🚨 EMERGENCY: Urgent Medical Incident - " + healthEvent.getStudent().getFullName();
            String content = buildEmergencyNotificationContent(healthEvent, parent);
            
            emailService.sendSimpleMessage(parent.getEmail(), subject, content);
            log.info("Emergency notification sent to parent {} for medical incident {}", parent.getEmail(), healthEvent.getEventId());
            
        } catch (Exception e) {
            log.error("Failed to send emergency notification to parent {} for medical incident {}", parentId, healthEvent.getEventId(), e);
        }
    }

    @Override
    public void sendApprovalReminder(HealthEvent healthEvent, UUID parentId) {
        try {
            Optional<Account> parentOpt = accountRepository.findById(parentId);
            if (parentOpt.isEmpty()) {
                log.error("Parent not found with ID: {}", parentId);
                return;
            }
            
            Account parent = parentOpt.get();
            String subject = "⏰ Reminder: Medical Incident Approval Required - " + healthEvent.getStudent().getFullName();
            String content = buildApprovalReminderContent(healthEvent, parent);
            
            emailService.sendSimpleMessage(parent.getEmail(), subject, content);
            log.info("Approval reminder sent to parent {} for medical incident {}", parent.getEmail(), healthEvent.getEventId());
            
        } catch (Exception e) {
            log.error("Failed to send approval reminder to parent {} for medical incident {}", parentId, healthEvent.getEventId(), e);
        }
    }

    @Override
    public void sendApprovalConfirmation(HealthEvent healthEvent, UUID parentId) {
        try {
            Optional<Account> parentOpt = accountRepository.findById(parentId);
            if (parentOpt.isEmpty()) {
                log.error("Parent not found with ID: {}", parentId);
                return;
            }
            
            Account parent = parentOpt.get();
            String subject = "✅ Medical Incident " + healthEvent.getParentApprovalStatus().getDisplayName() + " - " + healthEvent.getStudent().getFullName();
            String content = buildApprovalConfirmationContent(healthEvent, parent);
            
            emailService.sendSimpleMessage(parent.getEmail(), subject, content);
            log.info("Approval confirmation sent to parent {} for medical incident {}", parent.getEmail(), healthEvent.getEventId());
            
        } catch (Exception e) {
            log.error("Failed to send approval confirmation to parent {} for medical incident {}", parentId, healthEvent.getEventId(), e);
        }
    }

    @Override
    public boolean shouldSendNotification(HealthEventPriority priority) {
        return priority != null && priority.requiresParentNotification();
    }

    private String buildNotificationSubject(HealthEvent healthEvent) {
        String priorityEmoji = getPriorityEmoji(healthEvent.getPriority());
        return priorityEmoji + " Medical Incident Notification - " + healthEvent.getStudent().getFullName();
    }

    private String buildNotificationContent(HealthEvent healthEvent, Account parent) {
        StringBuilder content = new StringBuilder();
        content.append("Medical Incident Notification\n\n");
        content.append("Dear ").append(parent.getFullName()).append(",\n\n");
        content.append("A medical incident has occurred involving your child ").append(healthEvent.getStudent().getFullName()).append(".\n\n");
        
        content.append("Incident Details:\n");
        content.append("- Date: ").append(healthEvent.getEventDate()).append("\n");
        content.append("- Type: ").append(healthEvent.getEventType()).append("\n");
        content.append("- Priority: ").append(healthEvent.getPriority().getDisplayName()).append("\n");
        content.append("- Description: ").append(healthEvent.getDescription()).append("\n");
        if (healthEvent.getSolution() != null && !healthEvent.getSolution().isEmpty()) {
            content.append("- Solution Applied: ").append(healthEvent.getSolution()).append("\n");
        }
        
        if (healthEvent.getPriority().requiresParentApproval()) {
            content.append("\nAction Required:\n");
            content.append("This incident requires your approval. Please log into the system to review and approve/reject this medical incident.\n");
        }
        
        content.append("\nIf you have any questions, please contact the school nurse.\n\n");
        content.append("Best regards,\nSchool Medical Management System");
        
        return content.toString();
    }

    private String buildEmergencyNotificationContent(HealthEvent healthEvent, Account parent) {
        StringBuilder content = new StringBuilder();
        content.append("🚨 EMERGENCY MEDICAL INCIDENT\n\n");
        content.append("Dear ").append(parent.getFullName()).append(",\n\n");
        content.append("URGENT: A critical medical incident has occurred involving your child ").append(healthEvent.getStudent().getFullName()).append(".\n\n");
        
        content.append("Emergency Details:\n");
        content.append("- Date: ").append(healthEvent.getEventDate()).append("\n");
        content.append("- Type: ").append(healthEvent.getEventType()).append("\n");
        content.append("- Description: ").append(healthEvent.getDescription()).append("\n");
        if (healthEvent.getSolution() != null && !healthEvent.getSolution().isEmpty()) {
            content.append("- Immediate Action Taken: ").append(healthEvent.getSolution()).append("\n");
        }
        
        content.append("\nIMMEDIATE ACTION REQUIRED:\n");
        content.append("Please log into the system immediately to review and approve this emergency medical incident.\n\n");
        
        content.append("If you cannot access the system, please contact the school immediately.\n\n");
        content.append("Best regards,\nSchool Medical Management System");
        
        return content.toString();
    }

    private String buildApprovalReminderContent(HealthEvent healthEvent, Account parent) {
        StringBuilder content = new StringBuilder();
        content.append("⏰ Approval Reminder\n\n");
        content.append("Dear ").append(parent.getFullName()).append(",\n\n");
        content.append("This is a reminder that a medical incident involving your child ").append(healthEvent.getStudent().getFullName()).append(" is still waiting for your approval.\n\n");
        
        content.append("Incident Summary:\n");
        content.append("- Date: ").append(healthEvent.getEventDate()).append("\n");
        content.append("- Type: ").append(healthEvent.getEventType()).append("\n");
        content.append("- Description: ").append(healthEvent.getDescription()).append("\n");
        
        content.append("\nPlease review and approve now.\n\n");
        
        content.append("Best regards,\nSchool Medical Management System");
        
        return content.toString();
    }

    private String buildApprovalConfirmationContent(HealthEvent healthEvent, Account parent) {
        StringBuilder content = new StringBuilder();
        
        String statusEmoji = healthEvent.getParentApprovalStatus() == HealthEventApprovalStatus.APPROVED ? "✅" : "❌";
        String statusText = healthEvent.getParentApprovalStatus() == HealthEventApprovalStatus.APPROVED ? "APPROVED" : "REJECTED";
        
        content.append(statusEmoji).append(" Medical Incident ").append(statusText).append("\n\n");
        content.append("Dear ").append(parent.getFullName()).append(",\n\n");
        content.append("Your medical incident for ").append(healthEvent.getStudent().getFullName()).append(" has been ").append(statusText).append(".\n\n");
        
        content.append("Incident Details:\n");
        content.append("- Date: ").append(healthEvent.getEventDate()).append("\n");
        content.append("- Type: ").append(healthEvent.getEventType()).append("\n");
        content.append("- Description: ").append(healthEvent.getDescription()).append("\n");
        content.append("- Approval Date: ").append(healthEvent.getParentApprovalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        if (healthEvent.getParentApprovalReason() != null && !healthEvent.getParentApprovalReason().isEmpty()) {
            content.append("- Reason: ").append(healthEvent.getParentApprovalReason()).append("\n");
        }
        
        content.append("\nThank you for your prompt response.\n\n");
        content.append("Best regards,\nSchool Medical Management System");
        
        return content.toString();
    }

    private String getPriorityEmoji(HealthEventPriority priority) {
        switch (priority) {
            case LOW: return "🟢";
            case MEDIUM: return "🟡";
            case HIGH: return "🟠";
            case CRITICAL: return "🔴";
            default: return "ℹ️";
        }
    }
} 