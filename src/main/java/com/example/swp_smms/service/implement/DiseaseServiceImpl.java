package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Disease;
import com.example.swp_smms.model.payload.request.DiseaseRequest;
import com.example.swp_smms.model.payload.response.DiseaseResponse;
import com.example.swp_smms.repository.DiseaseRepository;
import com.example.swp_smms.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<DiseaseResponse> getAll() {
        return diseaseRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseResponse> searchByName(String name) {
        return diseaseRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    private DiseaseResponse toDto(Disease d) {
        return new DiseaseResponse(
                d.getDiseaseId(),
                d.getName(),
                d.getSeverityLevel(),
                d.isChronic(),
                d.isContagious()
        );
    }
}
