package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;
import com.example.swp_smms.service.VaccinationNoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vaccination-notices")
@RequiredArgsConstructor
public class VaccinationNoticeController {

    private final VaccinationNoticeService noticeService;

    @PostMapping
    public Object createNotice(@Valid @RequestBody VaccinationNoticeRequest request) {
        VaccinationNoticeResponse response = noticeService.createNotice(request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Vaccination notice created successfully", response);
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
} 