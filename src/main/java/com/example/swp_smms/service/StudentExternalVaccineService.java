package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.StudentExternalVaccineRequest;
import com.example.swp_smms.model.entity.StudentExternalVaccine;
import com.example.swp_smms.model.payload.response.StudentExternalVaccineResponse;

import java.util.List;
import java.util.UUID;

public interface StudentExternalVaccineService {
    StudentExternalVaccineResponse create(StudentExternalVaccineRequest request);

    StudentExternalVaccineResponse verify(Long id);
    List<StudentExternalVaccineResponse> getExternalVaccinesByStudentAndVerifiedStatus(UUID studentId, boolean status);
    List<StudentExternalVaccineResponse> getAllUnverified();

}
