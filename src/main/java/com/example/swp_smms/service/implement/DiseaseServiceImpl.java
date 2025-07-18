package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Disease;
import com.example.swp_smms.model.payload.request.DiseaseRequest;
import com.example.swp_smms.repository.DiseaseRepository;
import com.example.swp_smms.service.DiseaseService;
import org.springframework.stereotype.Service;

@Service
public class DiseaseServiceImpl implements DiseaseService {
    private final DiseaseRepository repository;

    public DiseaseServiceImpl(DiseaseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Disease create(DiseaseRequest request) {
        Disease disease = new Disease();
        disease.setName(request.getName());
        disease.setDescription(request.getDescription());
        disease.setSeverityLevel(request.getSeverityLevel());
        disease.setChronic(request.isChronic());
        disease.setContagious(request.isContagious());
        return repository.save(disease);
    }
}
