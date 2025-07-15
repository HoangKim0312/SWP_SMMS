package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Vaccine;
import com.example.swp_smms.model.payload.request.VaccineRequest;
import com.example.swp_smms.model.payload.response.VaccineResponse;
import com.example.swp_smms.repository.VaccineRepository;
import com.example.swp_smms.service.VaccineService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccineServiceImpl implements VaccineService {

    private final VaccineRepository vaccineRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<VaccineResponse> getAllVaccines() {
        List<Vaccine> vaccines = vaccineRepository.findAll();
        return vaccines.stream()
                .map(v -> modelMapper.map(v, VaccineResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public VaccineResponse createVaccine(VaccineRequest request) {
        Vaccine vaccine = modelMapper.map(request, Vaccine.class);
        Vaccine saved = vaccineRepository.save(vaccine);
        return modelMapper.map(saved, VaccineResponse.class);
    }
}
