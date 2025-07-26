package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.VaccinationConfirmationRequest;
import com.example.swp_smms.model.payload.request.VaccinationConfirmationStatusRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.NoticeStatisticalResponse;
import com.example.swp_smms.model.payload.response.VaccinationConfirmationResponse;
import com.example.swp_smms.service.VaccinationConfirmationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vaccination-confirmations")
@RequiredArgsConstructor
public class VaccinationConfirmationController {

    private final VaccinationConfirmationService confirmationService;

    @GetMapping("/{id}")
    public Object getConfirmationById(@PathVariable Long id) {
        VaccinationConfirmationResponse response = confirmationService.getConfirmationById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination confirmation fetched successfully", response);
    }

    @GetMapping
    public Object getAllConfirmations() {
        List<VaccinationConfirmationResponse> response = confirmationService.getAllConfirmations();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All vaccination confirmations fetched successfully", response);
    }

    @GetMapping("/student/{studentId}")
    public Object getConfirmationsByStudent(@PathVariable UUID studentId) {
        List<VaccinationConfirmationResponse> response = confirmationService.getConfirmationsByStudent(studentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination confirmations for student fetched successfully", response);
    }

    @GetMapping("/parent/{parentId}")
    public Object getConfirmationsByParent(@PathVariable UUID parentId) {
        List<VaccinationConfirmationResponse> response = confirmationService.getConfirmationsByParent(parentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination confirmations for parent fetched successfully", response);
    }

    @GetMapping("/notice/{vaccineNoticeId}")
    public Object getConfirmationsByNotice(@PathVariable Long vaccineNoticeId) {
        List<VaccinationConfirmationResponse> response = confirmationService.getConfirmationsByNotice(vaccineNoticeId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination confirmations for notice fetched successfully", response);
    }

    @GetMapping("/all-by-status/{status}")
    public Object getConfirmationsByStatus(@PathVariable String status) {
        List<VaccinationConfirmationResponse> response = confirmationService.getConfirmationsByStatus(status);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination confirmations by status fetched successfully", response);
    }

    @PutMapping("/update")
    public Object updateConfirmation(@Valid @RequestBody VaccinationConfirmationRequest request) {
        VaccinationConfirmationResponse response = confirmationService.updateConfirmation(request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination confirmation updated successfully", response);
    }

    @PutMapping("/update-status-only")
    public Object updateStatusOnly(@Valid @RequestBody VaccinationConfirmationStatusRequest request) {
        VaccinationConfirmationResponse response = confirmationService.updateStatusOnly(request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination status updated successfully", response);
    }


    @DeleteMapping("/{id}")
    public Object deleteConfirmation(@PathVariable Long id) {
        confirmationService.deleteConfirmation(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Vaccination confirmation deleted successfully");
    }

    @GetMapping("/notice/{vaccineNoticeId}/students/confirmed")
    public Object getConfirmedStudentsByNotice(@PathVariable Long vaccineNoticeId) {
        List<AccountResponse> response = confirmationService.getConfirmedStudentsByNotice(vaccineNoticeId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Confirmed students fetched successfully", response);
    }

    @GetMapping("/status")
    public Object getConfirmationsByStatusAndParent(
            @RequestParam String status,
            @RequestParam UUID parentId
    ) {
        List<VaccinationConfirmationResponse> response = confirmationService.getConfirmationsByStatusAndParentId(status, parentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Confirmations fetched successfully", response);
    }

    @PutMapping("/confirm-all")
    public Object confirmAllPendingByParent(@RequestParam UUID parentId) {
        int updatedCount = confirmationService.confirmAllPendingByParent(parentId);
        return ResponseBuilder.responseBuilderWithData(
                HttpStatus.OK,
                "Confirmed " + updatedCount + " pending confirmations for parent's children",
                updatedCount
        );
    }

    @GetMapping("/statistics/{noticeId}")
    public Object getNoticeStatistics(@PathVariable Long noticeId) {
        NoticeStatisticalResponse response = confirmationService.getStatusCountsByNoticeId(noticeId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Notice statistics fetched successfully", response);
    }



} 