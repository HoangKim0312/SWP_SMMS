package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.VaccineBatchRequest;
import com.example.swp_smms.model.payload.response.VaccineBatchResponse;

import java.util.List;

public interface VaccineBatchService {
    VaccineBatchResponse createBatch(Long vaccineId, VaccineBatchRequest request);

    List<VaccineBatchResponse> getAllBatchesByVaccineId(Long vaccineId);
    void reduceBatchQuantity(Long batchId, int quantityToReduce);

}
