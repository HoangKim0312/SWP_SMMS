package com.example.swp_smms.model.payload.request;

import com.example.swp_smms.model.enums.HealthEventApprovalStatus;
import com.example.swp_smms.model.enums.HealthEventPriority;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class HealthEventSearchRequest {
    // Search parameters
    private String searchTerm; // Search in eventType, description, solution, note
    private String eventType;
    private String eventDate;
    private HealthEventPriority priority;
    private HealthEventApprovalStatus approvalStatus;
    private String status;
    private Boolean requiresHomeCare;
    private UUID studentId;
    private UUID nurseId;
    private UUID parentId;
    
    // Date range
    private String startDate;
    private String endDate;
    
    // Sort parameters
    private String sortBy = "createdAt"; // Default sort by creation date
    private String sortDirection = "desc"; // Default descending order
    
    // Pagination parameters
    private Integer page = 0; // Default page 0
    private Integer size = 20; // Default page size 20
    
    // Valid sort fields
    public static final String[] VALID_SORT_FIELDS = {
        "eventId", "eventDate", "eventType", "priority", "status", 
        "parentApprovalStatus", "requiresHomeCare", "createdAt", "updatedAt",
        "studentName", "nurseName"
    };
    
    // Valid sort directions
    public static final String[] VALID_SORT_DIRECTIONS = {"asc", "desc"};
} 