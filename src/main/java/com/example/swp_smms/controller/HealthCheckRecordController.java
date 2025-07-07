package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.HealthCheckRecordRequest;
import com.example.swp_smms.model.payload.response.HealthCheckRecordResponse;
import com.example.swp_smms.service.HealthCheckRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/health-check-records")
@RequiredArgsConstructor
public class HealthCheckRecordController {

    private final HealthCheckRecordService recordService;

    @PostMapping("/create")
    public Object createRecord(@Valid @RequestBody HealthCheckRecordRequest request, 
                              @RequestParam UUID studentId, 
                              @RequestParam UUID nurseId) {
        HealthCheckRecordResponse response = recordService.createRecord(request, studentId, nurseId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check record created successfully", response);
    }

    @GetMapping("/getByID/{id}")
    public Object getRecordById(@PathVariable Long id) {
        HealthCheckRecordResponse response = recordService.getRecordById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check record fetched successfully", response);
    }

    @GetMapping("getAll")
    public Object getAllRecords() {
        List<HealthCheckRecordResponse> response = recordService.getAllRecords();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All health check records fetched successfully", response);
    }

    @GetMapping("/getByStudent/{studentId}")
    public Object getRecordsByStudent(@PathVariable UUID studentId) {
        List<HealthCheckRecordResponse> response = recordService.getRecordsByStudent(studentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check records for student fetched successfully", response);
    }

    @GetMapping("/getByNurse/{nurseId}")
    public Object getRecordsByNurse(@PathVariable UUID nurseId) {
        List<HealthCheckRecordResponse> response = recordService.getRecordsByNurse(nurseId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check records for nurse fetched successfully", response);
    }

    @GetMapping("/getByNotice/{checkNoticeId}")
    public Object getRecordsByNotice(@PathVariable Long checkNoticeId) {
        List<HealthCheckRecordResponse> response = recordService.getRecordsByNotice(checkNoticeId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check records for notice fetched successfully", response);
    }

    @GetMapping("/getByDate/{date}")
    public Object getRecordsByDate(@PathVariable String date) {
        List<HealthCheckRecordResponse> response = recordService.getRecordsByDate(date);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check records by date fetched successfully", response);
    }

    @GetMapping("/getByTitle/{title}")
    public Object getRecordsByTitle(@PathVariable String title) {
        List<HealthCheckRecordResponse> response = recordService.getRecordsByTitle(title);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check records by title fetched successfully", response);
    }

    @PutMapping("/update/{id}")
    public Object updateRecord(@PathVariable Long id, @Valid @RequestBody HealthCheckRecordRequest request) {
        HealthCheckRecordResponse response = recordService.updateRecord(id, request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check record updated successfully", response);
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Health check record deleted successfully");
    }
} 