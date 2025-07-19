package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.SyndromeDisability;
import com.example.swp_smms.model.payload.request.SyndromeDisabilityRequest;
import com.example.swp_smms.repository.SyndromeDisabilityRepository;
import com.example.swp_smms.service.SyndromeDisabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SyndromeDisabilityServiceImpl implements SyndromeDisabilityService {
    private final SyndromeDisabilityRepository syndromeDisabilityRepository;


    @Override
    public SyndromeDisability create(SyndromeDisabilityRequest request) {
        SyndromeDisability condition = new SyndromeDisability();
        condition.setName(request.getName());
        condition.setDescription(request.getDescription());
        condition.setType(request.getType());
        condition.setPriority(request.getPriority());
        return syndromeDisabilityRepository.save(condition);
    }
    @Override
    public List<SyndromeDisability> getAll() {
        return syndromeDisabilityRepository.findAll();
    }

    @Override
    public List<SyndromeDisability> searchByName(String name) {
        return syndromeDisabilityRepository.findByNameContainingIgnoreCase(name);
    }

}
