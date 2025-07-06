package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.HealthCheckNotice;
import com.example.swp_smms.model.payload.request.HealthCheckNoticeRequest;
import com.example.swp_smms.model.payload.response.HealthCheckNoticeResponse;
import com.example.swp_smms.repository.HealthCheckNoticeRepository;
import com.example.swp_smms.service.HealthCheckNoticeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        notice.setCreatedAt(currentDateTime);
        notice.setDate(currentDateTime);
        
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
    public List<HealthCheckNoticeResponse> getNoticesByDate(String date) {
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
    public HealthCheckNoticeResponse updateNotice(Long checkNoticeId, HealthCheckNoticeRequest request) {
        HealthCheckNotice notice = healthCheckNoticeRepository.findById(checkNoticeId)
                .orElseThrow(() -> new RuntimeException("Health check notice not found with id: " + checkNoticeId));
        
        // Update fields from request
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        
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