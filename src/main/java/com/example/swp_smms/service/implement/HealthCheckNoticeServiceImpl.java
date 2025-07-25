package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.HealthCheckNotice;
import com.example.swp_smms.model.payload.request.HealthCheckNoticeRequest;
import com.example.swp_smms.model.payload.response.HealthCheckNoticeResponse;
import com.example.swp_smms.repository.HealthCheckNoticeRepository;
import com.example.swp_smms.service.HealthCheckNoticeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthCheckNoticeServiceImpl implements HealthCheckNoticeService {
    
    @Autowired
    private HealthCheckNoticeRepository healthCheckNoticeRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HealthCheckNoticeResponse createNotice(HealthCheckNoticeRequest request) {
        // Map request to entity
        HealthCheckNotice notice = modelMapper.map(request, HealthCheckNotice.class);
        
        // Set current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        notice.setCreatedAt(currentDateTime);
        if (request.getDate() != null && !request.getDate().isEmpty()) {
            try {
                notice.setDate(LocalDate.parse(request.getDate()));
            } catch (Exception e) {
                throw new RuntimeException("Invalid date format: " + request.getDate());
            }
        }
        //Set tittle & desciption
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        // Set grade
        notice.setGrade(request.getGrade());
        // Set priority
        notice.setPriority(request.getPriority());
        // Save to DB
        HealthCheckNotice savedNotice = healthCheckNoticeRepository.save(notice);
        
        // Map to response
        HealthCheckNoticeResponse response = modelMapper.map(savedNotice, HealthCheckNoticeResponse.class);
        response.setCheckNoticeId(savedNotice.getCheckNoticeId());
        
        return response;
    }

    @Override
    public HealthCheckNoticeResponse getNoticeById(Long checkNoticeId) {
        HealthCheckNotice notice = healthCheckNoticeRepository.findById(checkNoticeId)
                .orElseThrow(() -> new RuntimeException("Health check notice not found with id: " + checkNoticeId));
        
        HealthCheckNoticeResponse response = modelMapper.map(notice, HealthCheckNoticeResponse.class);
        response.setCheckNoticeId(notice.getCheckNoticeId());
        
        return response;
    }

    @Override
    public List<HealthCheckNoticeResponse> getAllNotices() {
        List<HealthCheckNotice> notices = healthCheckNoticeRepository.findAll();
        return notices.stream()
                .map(notice -> {
                    HealthCheckNoticeResponse response = modelMapper.map(notice, HealthCheckNoticeResponse.class);
                    response.setCheckNoticeId(notice.getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckNoticeResponse> getNoticesByDate(LocalDate date) {
        List<HealthCheckNotice> notices = healthCheckNoticeRepository.findByDate(date);
        return notices.stream()
                .map(notice -> {
                    HealthCheckNoticeResponse response = modelMapper.map(notice, HealthCheckNoticeResponse.class);
                    response.setCheckNoticeId(notice.getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckNoticeResponse> getNoticesByTitle(String title) {
        List<HealthCheckNotice> notices = healthCheckNoticeRepository.findByTitleContainingIgnoreCase(title);
        return notices.stream()
                .map(notice -> {
                    HealthCheckNoticeResponse response = modelMapper.map(notice, HealthCheckNoticeResponse.class);
                    response.setCheckNoticeId(notice.getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckNoticeResponse> getNoticesByGrade(Integer grade) {
        List<HealthCheckNotice> notices = healthCheckNoticeRepository.findByGrade(grade);
        return notices.stream()
                .map(notice -> {
                    HealthCheckNoticeResponse response = modelMapper.map(notice, HealthCheckNoticeResponse.class);
                    response.setCheckNoticeId(notice.getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckNoticeResponse> getNoticesByPriority(String priority) {
        List<HealthCheckNotice> notices = healthCheckNoticeRepository.findByPriority(priority);
        return notices.stream()
                .map(notice -> {
                    HealthCheckNoticeResponse response = modelMapper.map(notice, HealthCheckNoticeResponse.class);
                    response.setCheckNoticeId(notice.getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public HealthCheckNoticeResponse updateNotice(Long checkNoticeId, HealthCheckNoticeRequest request) {
        HealthCheckNotice notice = healthCheckNoticeRepository.findById(checkNoticeId)
                .orElseThrow(() -> new RuntimeException("Health check notice not found with id: " + checkNoticeId));
        
        // Update fields from request
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        if (request.getDate() != null && !request.getDate().isEmpty()) {
            try {
                notice.setDate(LocalDate.parse(request.getDate()));
            } catch (Exception e) {
                throw new RuntimeException("Invalid date format: " + request.getDate());
            }
        }
        // Update grade
        notice.setGrade(request.getGrade());
        // Update priority
        notice.setPriority(request.getPriority());
        HealthCheckNotice updatedNotice = healthCheckNoticeRepository.save(notice);
        
        HealthCheckNoticeResponse response = modelMapper.map(updatedNotice, HealthCheckNoticeResponse.class);
        response.setCheckNoticeId(updatedNotice.getCheckNoticeId());
        
        return response;
    }

    @Override
    public void deleteNotice(Long checkNoticeId) {
        if (!healthCheckNoticeRepository.existsById(checkNoticeId)) {
            throw new RuntimeException("Health check notice not found with id: " + checkNoticeId);
        }
        healthCheckNoticeRepository.deleteById(checkNoticeId);
    }
} 