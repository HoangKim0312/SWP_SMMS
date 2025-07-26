# Medical Incident Priority & Parent Approval System

## Overview
This document describes the implementation of a comprehensive medical incident management system with priority-based notifications and parent approval workflows.

## New Features Implemented

### 1. Priority System
- **HealthEventPriority Enum**: Defines 4 priority levels (LOW, MEDIUM, HIGH, CRITICAL)
- **Priority-based Logic**: Determines notification requirements and approval needs
- **Auto-determination**: System can automatically determine home care requirements based on priority and event type

### 2. Parent Approval System
- **Approval Status Tracking**: PENDING, APPROVED, REJECTED, NOT_REQUIRED
- **Parent Login Integration**: Parents can log into the system to approve/reject medical incidents
- **Approval History**: Tracks who approved, when, and reasons

### 3. Email Notification System
- **Priority-based Notifications**: Different email templates for different priority levels
- **Emergency Notifications**: Special handling for CRITICAL priority incidents
- **Approval Reminders**: Automated reminders for pending approvals
- **Confirmation Emails**: Notifications when parents approve/reject incidents

### 4. Scenario-based Follow-up Creation
- **Automatic Follow-up**: Creates follow-ups based on priority and incident characteristics
- **Home Care Detection**: Automatically detects if home care is required
- **Instruction Generation**: Creates appropriate instructions based on incident details

## Priority Levels & Business Logic

### LOW Priority
- **Description**: Can be handled at school, no parent notification needed
- **Examples**: Minor scrapes, small cuts, mild headaches
- **Actions**: 
  - No parent notification
  - No approval required
  - No follow-up created
  - Handled entirely at school

### MEDIUM Priority
- **Description**: Requires parent notification via email, approval needed
- **Examples**: Fever, vomiting, minor injuries
- **Actions**:
  - Email notification sent to parent
  - Parent approval required
  - Follow-up created with monitoring instructions
  - Parent must log into system to approve

### HIGH Priority
- **Description**: Requires immediate parent notification via email, urgent approval needed
- **Examples**: Serious injuries, severe symptoms, medication administration
- **Actions**:
  - Immediate email notification
  - Urgent parent approval required
  - Follow-up created with doctor consultation recommendation
  - Home care instructions provided

### CRITICAL Priority
- **Description**: Emergency notification, immediate action required
- **Examples**: Severe bleeding, unconsciousness, allergic reactions
- **Actions**:
  - Emergency email notification
  - Immediate parent approval required
  - Follow-up created with emergency instructions
  - Doctor consultation strongly recommended

## API Endpoints

### New Medical Incident Creation (Enhanced)
```
POST /api/v1/healthEvents/create/{studentId}/{nurseId}
```
**Features**:
- Priority-based automatic actions
- Scenario-based follow-up creation
- Parent notification based on priority
- Home care requirement detection

**Request Body**:
```json
{
  "eventDate": "2024-01-15",
  "eventType": "Injury",
  "description": "Student fell and scraped knee",
  "solution": "Applied bandage and antiseptic",
  "note": "Minor injury, no serious concern",
  "status": "RESOLVED",
  "priority": "MEDIUM",
  "requiresHomeCare": true,
  "medications": [
    {
      "medicationId": 1,
      "dosageAmount": 1,
      "dosageUnit": "tablet",
      "frequency": "once daily",
      "duration": "3 days",
      "administrationNotes": "Take with food"
    }
  ],
  "followUp": {
    "instruction": "Monitor for infection",
    "requiresDoctor": false,
    "status": "PENDING"
  }
}
```

### Parent Approval
```
PUT /api/v1/healthEvents/approve
```
**Request Body**:
```json
{
  "eventId": 123,
  "parentId": "uuid-here",
  "approvalStatus": "APPROVED",
  "reason": "Agree with the treatment plan"
}
```

### Get Incidents by Priority
```
GET /api/v1/healthEvents/priority/{priority}
```
**Priority Values**: LOW, MEDIUM, HIGH, CRITICAL

### Get Pending Approvals for Parent
```
GET /api/v1/healthEvents/parent/{parentId}/pending-approval
```

## Email Notification Templates

### Standard Notification (MEDIUM/HIGH)
- Incident details
- Priority level
- Action required (login to approve)
- Contact information

### Emergency Notification (CRITICAL)
- Emergency header
- Urgent action required
- Immediate login instructions
- Emergency contact information

### Approval Reminder
- Reminder about pending approval
- Incident summary
- Direct link to approval page

### Approval Confirmation
- Approval status (APPROVED/REJECTED)
- Incident details
- Approval timestamp
- Reason (if provided)

## Database Schema Changes

### HealthEvent Entity Updates
```sql
ALTER TABLE health_event ADD COLUMN priority VARCHAR(20);
ALTER TABLE health_event ADD COLUMN parent_approval_status VARCHAR(20);
ALTER TABLE health_event ADD COLUMN parent_approval_reason TEXT;
ALTER TABLE health_event ADD COLUMN parent_approval_date TIMESTAMP;
ALTER TABLE health_event ADD COLUMN approved_by_parent_id UUID;
ALTER TABLE health_event ADD COLUMN requires_home_care BOOLEAN;
ALTER TABLE health_event ADD COLUMN created_at TIMESTAMP;
ALTER TABLE health_event ADD COLUMN updated_at TIMESTAMP;
```

## Business Flow Examples

### Example 1: Minor Incident (LOW Priority)
1. Student has a small scrape
2. Nurse creates medical incident with LOW priority
3. System handles at school level
4. No parent notification
5. No follow-up created
6. Incident logged for record keeping

### Example 2: Fever (MEDIUM Priority)
1. Student has fever
2. Nurse creates medical incident with MEDIUM priority
3. System sends email notification to parent
4. Parent receives email with login instructions
5. Parent logs into system and approves
6. Follow-up created with monitoring instructions
7. Confirmation email sent to parent

### Example 3: Serious Injury (HIGH Priority)
1. Student has serious injury
2. Nurse creates medical incident with HIGH priority
3. System sends urgent email notification
4. Parent receives emergency notification
5. Parent logs into system immediately
6. Parent approves with reason
7. Follow-up created with doctor consultation recommendation
8. Confirmation email sent

### Example 4: Allergic Reaction (CRITICAL Priority)
1. Student has allergic reaction
2. Nurse creates medical incident with CRITICAL priority
3. System sends emergency email notification
4. Parent receives emergency notification immediately
5. Parent logs into system urgently
6. Parent approves with emergency instructions
7. Follow-up created with emergency care instructions
8. Doctor consultation strongly recommended

## Configuration

### Email Settings
- Configure `SYSTEM_BASE_URL` in `HealthEventNotificationServiceImpl`
- Update email templates as needed
- Configure email service settings in `application.properties`

### Priority Keywords
- Home care detection keywords can be customized in `determineHomeCareRequirement()` method
- Follow-up instruction templates can be modified in `generateFollowUpInstruction()` method

## Security Considerations

### Parent Authorization
- Parents can only approve incidents for their own children
- System validates parent-student relationship before allowing approval
- All approval actions are logged with timestamps

### Data Privacy
- Email notifications contain only necessary information
- Parent approval reasons are stored for audit purposes
- All sensitive data is properly encrypted

## Future Enhancements

### Potential Improvements
1. **SMS Notifications**: Add SMS for critical incidents
2. **Push Notifications**: Mobile app notifications
3. **Automated Reminders**: Scheduled reminders for pending approvals
4. **Escalation Logic**: Automatic escalation for unapproved critical incidents
5. **Analytics Dashboard**: Priority-based reporting and analytics
6. **Multi-language Support**: Email templates in multiple languages

### Integration Points
1. **Calendar Integration**: Schedule follow-up appointments
2. **Doctor Portal**: Direct communication with healthcare providers
3. **Insurance Integration**: Automatic insurance claim processing
4. **Emergency Services**: Direct integration for critical incidents

## Testing Scenarios

### Test Cases
1. **LOW Priority Incident**: Verify no notifications sent
2. **MEDIUM Priority Incident**: Verify email sent and approval required
3. **HIGH Priority Incident**: Verify urgent notification and follow-up creation
4. **CRITICAL Priority Incident**: Verify emergency notification and immediate action
5. **Parent Approval**: Verify approval workflow and confirmation
6. **Parent Rejection**: Verify rejection workflow and reason tracking
7. **Multiple Parents**: Verify all parents receive notifications
8. **No Parent Linked**: Verify proper error handling

## Monitoring & Alerts

### Key Metrics
- Notification delivery rates
- Parent approval response times
- Priority distribution of incidents
- Follow-up completion rates
- System performance metrics

### Alerts
- Failed email notifications
- Unapproved critical incidents after time threshold
- System errors in approval workflow
- High volume of incidents in specific priority levels 