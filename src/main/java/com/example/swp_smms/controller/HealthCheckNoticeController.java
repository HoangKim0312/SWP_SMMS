package com.example.swp_smms.controller;

import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.model.payload.request.HealthCheckNoticeRequest;
import com.example.swp_smms.model.payload.response.HealthCheckNoticeResponse;
import com.example.swp_smms.service.HealthCheckNoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/health-check-notices")
@RequiredArgsConstructor
public class HealthCheckNoticeController {

    private final HealthCheckNoticeService noticeService;

    @PostMapping("/create")
    public Object createNotice(@Valid @RequestBody HealthCheckNoticeRequest request) {
        HealthCheckNoticeResponse response = noticeService.createNotice(request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check notice created successfully", response);
    }

    @GetMapping("/getByID/{id}")
    public Object getNoticeById(@PathVariable Long id) {
        HealthCheckNoticeResponse response = noticeService.getNoticeById(id);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check notice fetched successfully", response);
    }

    @GetMapping("/getAll")
    public Object getAllNotices() {
        List<HealthCheckNoticeResponse> response = noticeService.getAllNotices();
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "All health check notices fetched successfully", response);
    }

    @GetMapping("/getBydate/{date}")
    public Object getNoticesByDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        List<HealthCheckNoticeResponse> response = noticeService.getNoticesByDate(parsedDate);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check notices by date fetched successfully", response);
    }

    @GetMapping("/getByTitle/{title}")
    public Object getNoticesByTitle(@PathVariable String title) {
        List<HealthCheckNoticeResponse> response = noticeService.getNoticesByTitle(title);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check notices by title fetched successfully", response);
    }

    @GetMapping("/getByGrade/{grade}")
    public Object getNoticesByGrade(@PathVariable Integer grade) {
        List<HealthCheckNoticeResponse> response = noticeService.getNoticesByGrade(grade);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check notices by grade fetched successfully", response);
    }

    @GetMapping("/getByPriority/{priority}")
    public Object getNoticesByPriority(@PathVariable String priority) {
        List<HealthCheckNoticeResponse> response = noticeService.getNoticesByPriority(priority);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check notices by priority fetched successfully", response);
    }

    @PutMapping("/update/{id}")
    public Object updateNotice(@PathVariable Long id, @Valid @RequestBody HealthCheckNoticeRequest request) {
        HealthCheckNoticeResponse response = noticeService.updateNotice(id, request);
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Health check notice updated successfully", response);
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Health check notice deleted successfully");
    }
} 