package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Vaccine;
import com.example.swp_smms.model.entity.VaccineBatch;
import com.example.swp_smms.model.payload.request.VaccineBatchRequest;
import com.example.swp_smms.model.payload.response.VaccineBatchResponse;
import com.example.swp_smms.repository.VaccineBatchRepository;
import com.example.swp_smms.repository.VaccineRepository;
import com.example.swp_smms.service.VaccineBatchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccineBatchServiceImpl implements VaccineBatchService {

    private final VaccineRepository vaccineRepository;
    private final VaccineBatchRepository vaccineBatchRepository;
    private final ModelMapper modelMapper;

    @Override
    public VaccineBatchResponse createBatch(Long vaccineId, VaccineBatchRequest request) {
        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with ID: " + vaccineId));

        VaccineBatch batch = new VaccineBatch();
        batch.setVaccineId(vaccineId);
        batch.setStockInDate(request.getStockInDate());
        batch.setExpiryDate(request.getExpiryDate());
        batch.setQuantity(request.getQuantity()); // SET quantity

        VaccineBatch saved = vaccineBatchRepository.save(batch);
        return modelMapper.map(saved, VaccineBatchResponse.class);
    }

    @Override
    public List<VaccineBatchResponse> getAllBatchesByVaccineId(Long vaccineId) {
        List<VaccineBatch> batches = vaccineBatchRepository.findActiveByVaccineId(vaccineId);
        return batches.stream()
                .map(batch -> modelMapper.map(batch, VaccineBatchResponse.class))
                .collect(Collectors.toList());
    }


    @Override
    public void reduceBatchQuantity(Long batchId, int quantityToReduce) {
        VaccineBatch batch = vaccineBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Vaccine batch not found"));

        if (batch.getQuantity() < quantityToReduce) {
            throw new RuntimeException("Not enough quantity in this batch.");
        }

        batch.setQuantity(batch.getQuantity() - quantityToReduce);
        vaccineBatchRepository.save(batch);
    }

}
