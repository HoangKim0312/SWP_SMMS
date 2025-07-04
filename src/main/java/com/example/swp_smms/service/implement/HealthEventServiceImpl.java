package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.HealthEvent;
import com.example.swp_smms.model.payload.request.HealthEventRequest;
import com.example.swp_smms.model.payload.response.HealthEventResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.HealthEventRepository;
import com.example.swp_smms.service.HealthEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HealthEventServiceImpl implements HealthEventService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private HealthEventRepository healthEventRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HealthEventResponse createHealthEvent(UUID studentId, UUID nurseId, HealthEventRequest request) {
        // Validate student exists
        if (!accountRepository.existsByAccountIdAndRole_RoleId(studentId, 1L)) {
            throw new RuntimeException("Student not exists or not a student");
        }
        
        // Validate nurse exists
        if (!accountRepository.existsByAccountIdAndRole_RoleId(nurseId, 3L)) {
            throw new RuntimeException("Nurse not exists or not a nurse");
        }
        
        // Map request to entity
        HealthEvent event = modelMapper.map(request, HealthEvent.class);
        
        // Set student and nurse entities
        Account student = accountRepository.findAccountByAccountId(studentId);
        Account nurse = accountRepository.findAccountByAccountId(nurseId);
        event.setStudent(student);
        event.setNurse(nurse);
        
        // Save to DB
        healthEventRepository.save(event);
        
        // Map to response
        HealthEventResponse response = modelMapper.map(event, HealthEventResponse.class);
        response.setStudentID(studentId);
        response.setNurseID(nurseId);
        
        return response;
    }

    @Override
    public List<HealthEventResponse> viewAllHealthEvents() {
        List<HealthEvent> events = healthEventRepository.findAll();
        return events.stream()
                .map(event -> {
                    HealthEventResponse response = modelMapper.map(event, HealthEventResponse.class);
                    response.setStudentID(event.getStudent().getAccountId());
                    if (event.getNurse() != null) {
                        response.setNurseID(event.getNurse().getAccountId());
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthEventResponse> viewHealthEventsByDate(String eventDate) {
        List<HealthEvent> events = healthEventRepository.findByEventDate(eventDate);
        return events.stream()
                .map(event -> {
                    HealthEventResponse response = modelMapper.map(event, HealthEventResponse.class);
                    response.setStudentID(event.getStudent().getAccountId());
                    if (event.getNurse() != null) {
                        response.setNurseID(event.getNurse().getAccountId());
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }
}
