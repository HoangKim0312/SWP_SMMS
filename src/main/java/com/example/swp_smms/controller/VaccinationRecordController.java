package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.VaccinationRecordRequest;
import com.example.swp_smms.model.payload.response.VaccinationRecordResponse;
import com.example.swp_smms.service.VaccinationRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vaccination-records")
@RequiredArgsConstructor
public class VaccinationRecordController {

    private final VaccinationRecordService recordService;

    @PostMapping
    public Object createRecord(
            @Valid @RequestBody VaccinationRecordRequest request,
            @RequestParam UUID nurseId) {
        VaccinationRecordResponse response = recordService.createRecord(request, nurseId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination record created successfully", response);
    }

    @GetMapping("/{id}")
    public Object getRecordById(@PathVariable Long id) {
        VaccinationRecordResponse response = recordService.getRecordById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination record fetched successfully", response);
    }

    @GetMapping
    public Object getAllRecords() {
        List<VaccinationRecordResponse> response = recordService.getAllRecords();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All vaccination records fetched successfully", response);
    }

    @GetMapping("/student/{studentId}")
    public Object getRecordsByStudent(@PathVariable UUID studentId) {
        List<VaccinationRecordResponse> response = recordService.getRecordsByStudent(studentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination records for student fetched successfully", response);
    }

    // Xem kết quả tiêm chủng: Theo dõi kết quả tiêm chủng của học sinh
    @GetMapping("/student/{studentId}/results")
    public Object viewVaccinationResultsForStudent(@PathVariable UUID studentId) {
        List<VaccinationRecordResponse> response = recordService.getRecordsByStudent(studentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination results for student fetched successfully", response);
    }

    @GetMapping("/nurse/{nurseId}")
    public Object getRecordsByNurse(@PathVariable UUID nurseId) {
        List<VaccinationRecordResponse> response = recordService.getRecordsByNurse(nurseId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination records for nurse fetched successfully", response);
    }

    @GetMapping("/notice/{vaccineNoticeId}")
    public Object getRecordsByNotice(@PathVariable Long vaccineNoticeId) {
        List<VaccinationRecordResponse> response = recordService.getRecordsByNotice(vaccineNoticeId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination records for notice fetched successfully", response);
    }

    @PutMapping("/{id}")
    public Object updateRecord(@PathVariable Long id, @Valid @RequestBody VaccinationRecordRequest request) {
        VaccinationRecordResponse response = recordService.updateRecord(id, request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination record updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public Object deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Vaccination record deleted successfully");
    }
}