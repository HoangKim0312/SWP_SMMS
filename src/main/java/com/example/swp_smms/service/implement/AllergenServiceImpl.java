package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Allergen;
import com.example.swp_smms.model.payload.request.AllergenRequest;
import com.example.swp_smms.repository.AllergenRepository;
import com.example.swp_smms.service.AllergenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllergenServiceImpl implements AllergenService {
    private final AllergenRepository allergenRepository;

    @Override
    public Allergen create(AllergenRequest request) {
        Allergen allergen = new Allergen();
        allergen.setName(request.getName());
        return allergenRepository.save(allergen);
    }
    @Override
    public List<Allergen> searchByName(String name) {
        return allergenRepository.findByNameContainingIgnoreCase(name);
    }
    @Override
    public List<Allergen> getAll() {
        return allergenRepository.findAll();
    }
}
