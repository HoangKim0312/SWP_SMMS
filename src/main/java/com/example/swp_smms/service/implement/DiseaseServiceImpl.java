package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Disease;
import com.example.swp_smms.model.payload.request.DiseaseRequest;
import com.example.swp_smms.repository.DiseaseRepository;
import com.example.swp_smms.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {
    private final DiseaseRepository diseaseRepository;


    @Override
    public Disease create(DiseaseRequest request) {
        Disease disease = new Disease();
        disease.setName(request.getName());
        disease.setDescription(request.getDescription());
        disease.setSeverityLevel(request.getSeverityLevel());
        disease.setChronic(request.isChronic());
        disease.setContagious(request.isContagious());
        return diseaseRepository.save(disease);
    }
    @Override
    public List<Disease> getAll() {
        return diseaseRepository.findAll();
    }

    @Override
    public List<Disease> searchByName(String name) {
        return diseaseRepository.findByNameContainingIgnoreCase(name);
    }

}
