package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;
import com.example.swp_smms.service.VaccinationNoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vaccination-notices")
@RequiredArgsConstructor
public class VaccinationNoticeController {

    private final VaccinationNoticeService noticeService;

    @PostMapping
    public ResponseEntity<VaccinationNoticeResponse> createNotice(
            @RequestBody @Valid VaccinationNoticeRequest request,
            @RequestParam Long vaccineBatchId
    ) {
        VaccinationNoticeResponse created = noticeService.createNotice(request, vaccineBatchId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public Object getNoticeById(@PathVariable Long id) {
        VaccinationNoticeResponse response = noticeService.getNoticeById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination notice fetched successfully", response);
    }

    @GetMapping
    public Object getAllNotices() {
        List<VaccinationNoticeResponse> response = noticeService.getAllNotices();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All vaccination notices fetched successfully", response);
    }

    @GetMapping("/search")
    public Object searchNotices(@RequestParam String vaccineName) {
        List<VaccinationNoticeResponse> response = noticeService.searchNoticesByVaccineName(vaccineName);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination notices fetched successfully", response);
    }

    @PutMapping("/{id}")
    public Object updateNotice(@PathVariable Long id, @Valid @RequestBody VaccinationNoticeRequest request) {
        VaccinationNoticeResponse response = noticeService.updateNotice(id, request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination notice updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public Object deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Vaccination notice deleted successfully");
    }

    @GetMapping("/today")
    public Object getNoticesForToday() {
        List<VaccinationNoticeResponse> response = noticeService.getNoticesForToday();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Today's vaccination notices fetched successfully", response);
    }

    @GetMapping("/active")
    public Object getActiveNotices() {
        List<VaccinationNoticeResponse> response = noticeService.getActiveNotices();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Active (upcoming) vaccination notices fetched successfully", response);
    }

    @GetMapping("/active-by-parent")
    public Object getActiveNoticesByParent(@RequestParam UUID parentId) {
        List<VaccinationNoticeResponse> response = noticeService.getActiveNoticesByParent(parentId);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Active notices for parent's children fetched", response);
    }



} 