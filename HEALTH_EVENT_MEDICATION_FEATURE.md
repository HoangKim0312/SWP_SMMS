# Health Event Medication Feature

## Overview
The Health Event system has been enhanced to include detailed medication usage and dosage information. This allows nurses to record what medications were used during specific health events, including dosage amounts, frequency, duration, and administration notes.

## New Entities

### HealthEventMedication
- **health_event_medication_id**: Primary key
- **health_event_id**: Foreign key to HealthEvent
- **medication_id**: Foreign key to Medication
- **dosage_amount**: Amount of medication used (e.g., 2)
- **dosage_unit**: Unit of measurement (e.g., "tablet", "ml", "mg")
- **frequency**: How often the medication should be taken (e.g., "3 times daily")
- **duration**: How long the medication should be taken (e.g., "7 days")
- **administration_notes**: Special instructions (e.g., "Take with food")
- **usage_date**: Date when medication was used
- **usage_time**: Time when medication was used (e.g., "09:00", "after_meal")

## Updated API Endpoints

### Create Health Event with Medications
```
POST /api/v1/healthEvents/create/{studentId}/{nurseId}
```
Request body now includes a `medications` array:
```json
{
  "eventDate": "2024-01-15",
  "eventType": "Fever",
  "description": "Student has high fever",
  "solution": "Administered medication",
  "note": "Monitor temperature",
  "status": "Active",
  "medications": [
    {
      "medicationId": 1,
      "dosageAmount": 2,
      "dosageUnit": "tablet",
      "frequency": "3 times daily",
      "duration": "7 days",
      "administrationNotes": "Take with food",
      "usageDate": "2024-01-15",
      "usageTime": "09:00"
    }
  ]
}
```

### Get Medications for a Health Event
```
GET /api/v1/healthEvents/{eventId}/medications
```
Returns detailed medication information for a specific health event.

### Update Health Event with Medications
```
PUT /api/v1/healthEvents/update/{eventId}
```
When updating a health event, the medications array will replace all existing medications for that event.

## Database Changes

### New Table: health_event_medication
```sql
CREATE TABLE health_event_medication (
    health_event_medication_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    health_event_id BIGINT NOT NULL,
    medication_id BIGINT NOT NULL,
    dosage_amount INT,
    dosage_unit VARCHAR(50),
    frequency VARCHAR(100),
    duration VARCHAR(100),
    administration_notes TEXT,
    usage_date VARCHAR(20),
    usage_time VARCHAR(50),
    FOREIGN KEY (health_event_id) REFERENCES health_event(event_id),
    FOREIGN KEY (medication_id) REFERENCES medication(medication_id)
);
```

### Updated Table: health_event
The health_event table now has a one-to-many relationship with health_event_medication.

## Usage Examples

### Creating a Health Event with Multiple Medications
```json
{
  "eventDate": "2024-01-15",
  "eventType": "Injury",
  "description": "Student fell and scraped knee",
  "solution": "Cleaned wound and applied medication",
  "note": "Monitor for infection",
  "status": "Active",
  "medications": [
    {
      "medicationId": 1,
      "dosageAmount": 1,
      "dosageUnit": "tablet",
      "frequency": "once daily",
      "duration": "3 days",
      "administrationNotes": "Take with food",
      "usageDate": "2024-01-15",
      "usageTime": "14:30"
    },
    {
      "medicationId": 2,
      "dosageAmount": 5,
      "dosageUnit": "ml",
      "frequency": "twice daily",
      "duration": "5 days",
      "administrationNotes": "Apply to affected area",
      "usageDate": "2024-01-15",
      "usageTime": "14:30"
    }
  ]
}
```

### Response Format
All health event responses now include medication information:
```json
{
  "eventId": 1,
  "eventDate": "2024-01-15",
  "eventType": "Injury",
  "description": "Student fell and scraped knee",
  "solution": "Cleaned wound and applied medication",
  "note": "Monitor for infection",
  "status": "Active",
  "studentID": "uuid-here",
  "nurseID": "uuid-here",
  "medications": [
    {
      "healthEventMedicationId": 1,
      "medicationId": 1,
      "medicationName": "Paracetamol",
      "medicationDescription": "Pain reliever",
      "dosageAmount": 1,
      "dosageUnit": "tablet",
      "frequency": "once daily",
      "duration": "3 days",
      "administrationNotes": "Take with food",
      "usageDate": "2024-01-15",
      "usageTime": "14:30"
    }
  ]
}
```

## Benefits
1. **Complete Medical Record**: Each health event now includes detailed medication information
2. **Dosage Tracking**: Precise dosage amounts and units are recorded
3. **Administration Instructions**: Special notes and timing information are preserved
4. **Medication History**: Easy to see what medications were used for specific health events
5. **Audit Trail**: Complete record of medication usage for compliance and safety 