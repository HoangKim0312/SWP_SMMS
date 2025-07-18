package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.SyndromeDisability;
import com.example.swp_smms.model.payload.request.SyndromeDisabilityRequest;
import com.example.swp_smms.repository.SyndromeDisabilityRepository;
import com.example.swp_smms.service.SyndromeDisabilityService;
import org.springframework.stereotype.Service;

@Service
public class SyndromeDisabilityServiceImpl implements SyndromeDisabilityService {
    private final SyndromeDisabilityRepository repository;

    public SyndromeDisabilityServiceImpl(SyndromeDisabilityRepository repository) {
        this.repository = repository;
    }

    @Override
    public SyndromeDisability create(SyndromeDisabilityRequest request) {
        SyndromeDisability condition = new SyndromeDisability();
        condition.setName(request.getName());
        condition.setDescription(request.getDescription());
        condition.setType(request.getType());
        condition.setPriority(request.getPriority());
        return repository.save(condition);
    }
}
