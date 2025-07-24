package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.payload.request.StudentExternalVaccineRequest;
import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.StudentExternalVaccine;
import com.example.swp_smms.model.entity.Vaccine;
import com.example.swp_smms.model.payload.response.StudentExternalVaccineResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.StudentExternalVaccineRepository;
import com.example.swp_smms.repository.VaccineRepository;
import com.example.swp_smms.service.StudentExternalVaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentExternalVaccineServiceImpl implements StudentExternalVaccineService {

    @Autowired
    private StudentExternalVaccineRepository externalVaccineRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VaccineRepository vaccineRepository;
    @Override
    public StudentExternalVaccineResponse create(StudentExternalVaccineRequest request) {
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Account submitter = accountRepository.findById(request.getSubmittedBy())
                .orElseThrow(() -> new RuntimeException("submitter not found"));


        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId())
                .orElseThrow(() -> new RuntimeException("Vaccine not found"));

        StudentExternalVaccine externalVaccine = new StudentExternalVaccine();
        externalVaccine.setStudent(student);
        externalVaccine.setSubmitted_by(submitter);
        externalVaccine.setVaccine(vaccine);
        externalVaccine.setInjectionDate(request.getInjectionDate());
        externalVaccine.setLocation(request.getLocation());
        externalVaccine.setNote(request.getNote());
        externalVaccine.setVerified(false); // default: not verified

        StudentExternalVaccine saved = externalVaccineRepository.save(externalVaccine);

        return mapToResponse(saved);
    }


    @Override
    public StudentExternalVaccineResponse verify(Long id) {
        StudentExternalVaccine vaccineExternalRecord = externalVaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("External vaccine not found"));

        if (vaccineExternalRecord.isVerified()) {
            throw new RuntimeException("This record is already verified");
        }

        vaccineExternalRecord.setVerified(true);
        externalVaccineRepository.save(vaccineExternalRecord);
        return mapToResponse(vaccineExternalRecord);

    }
    @Override
    public List<StudentExternalVaccineResponse> getExternalVaccinesByStudentAndVerifiedStatus(UUID studentId, boolean status) {
        List<StudentExternalVaccine> records = externalVaccineRepository
                .findByStudent_AccountIdAndVerified(studentId, status);
        return records.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    private StudentExternalVaccineResponse mapToResponse(StudentExternalVaccine entity) {
        return StudentExternalVaccineResponse.builder()
                .externalVaccineId(entity.getId())
                .studentId(entity.getStudent().getAccountId())
                .submittedBy(entity.getSubmitted_by().getAccountId())
                .injectionDate(entity.getInjectionDate())
                .location(entity.getLocation())
                .note(entity.getNote())
                .verified(entity.isVerified())
                .build();
    }


}
