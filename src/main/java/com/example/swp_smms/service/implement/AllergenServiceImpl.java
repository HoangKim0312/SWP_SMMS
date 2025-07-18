package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Allergen;
import com.example.swp_smms.model.payload.request.AllergenRequest;
import com.example.swp_smms.repository.AllergenRepository;
import com.example.swp_smms.service.AllergenService;
import org.springframework.stereotype.Service;

@Service
public class AllergenServiceImpl implements AllergenService {
    private final AllergenRepository repository;

    public AllergenServiceImpl(AllergenRepository repository) {
        this.repository = repository;
    }

    @Override
    public Allergen create(AllergenRequest request) {
        Allergen allergen = new Allergen();
        allergen.setName(request.getName());
        return repository.save(allergen);
    }
}
