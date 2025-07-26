package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.HealthCheckNotice;
import com.example.swp_smms.model.entity.HealthCheckRecord;
import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.payload.request.HealthCheckRecordRequest;
import com.example.swp_smms.model.payload.response.HealthCheckRecordResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.HealthCheckNoticeRepository;
import com.example.swp_smms.repository.HealthCheckRecordRepository;
import com.example.swp_smms.repository.MedicalProfileRepository;
import com.example.swp_smms.service.HealthCheckRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class HealthCheckRecordServiceImpl implements HealthCheckRecordService {
    
    @Autowired
    private HealthCheckRecordRepository healthCheckRecordRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private HealthCheckNoticeRepository healthCheckNoticeRepository;
    
    @Autowired
    private MedicalProfileRepository medicalProfileRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HealthCheckRecordResponse createRecord(HealthCheckRecordRequest request, UUID studentId, UUID nurseId) {
        Account student = accountRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Account nurse = accountRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + nurseId));

        HealthCheckNotice notice = healthCheckNoticeRepository.findById(request.getHealthCheckNoticeId())
                .orElseThrow(() -> new RuntimeException("Health check notice not found with id: " + request.getHealthCheckNoticeId()));

        MedicalProfile medicalProfile = medicalProfileRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Medical profile not found for student."));

        HealthCheckRecord record = new HealthCheckRecord();
        record.setStudent(student);
        record.setNurse(nurse);
        record.setHealthCheckNotice(notice);
        record.setMedicalProfile(medicalProfile);
        record.setResults(request.getResult());
        if (request.getDate() != null) {
            record.setDate(LocalDate.parse(request.getDate()));
        }

        HealthCheckRecord savedRecord = healthCheckRecordRepository.save(record);
        return modelMapper.map(savedRecord, HealthCheckRecordResponse.class);
    }

    @Override
    public HealthCheckRecordResponse getRecordById(Long recordId) {
        HealthCheckRecord record = healthCheckRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Health check record not found with id: " + recordId));
        
        HealthCheckRecordResponse response = modelMapper.map(record, HealthCheckRecordResponse.class);
        response.setRecordId(record.getRecordId());
        response.setStudentId(record.getStudent().getAccountId());
        response.setNurseId(record.getNurse().getAccountId());
        response.setCheckNoticeId(record.getHealthCheckNotice().getCheckNoticeId());
        
        return response;
    }

    @Override
    public List<HealthCheckRecordResponse> getAllRecords() {
        List<HealthCheckRecord> records = healthCheckRecordRepository.findAll();
        return records.stream()
                .map(record -> {
                    HealthCheckRecordResponse response = modelMapper.map(record, HealthCheckRecordResponse.class);
                    response.setRecordId(record.getRecordId());
                    response.setStudentId(record.getStudent().getAccountId());
                    response.setNurseId(record.getNurse().getAccountId());
                    response.setCheckNoticeId(record.getHealthCheckNotice().getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckRecordResponse> getRecordsByStudent(UUID studentId) {
        List<HealthCheckRecord> records = healthCheckRecordRepository.findByStudent_AccountId(studentId);
        return records.stream()
                .map(record -> {
                    HealthCheckRecordResponse response = modelMapper.map(record, HealthCheckRecordResponse.class);
                    response.setRecordId(record.getRecordId());
                    response.setStudentId(record.getStudent().getAccountId());
                    response.setNurseId(record.getNurse().getAccountId());
                    response.setCheckNoticeId(record.getHealthCheckNotice().getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckRecordResponse> getRecordsByNurse(UUID nurseId) {
        List<HealthCheckRecord> records = healthCheckRecordRepository.findByNurse_AccountId(nurseId);
        return records.stream()
                .map(record -> {
                    HealthCheckRecordResponse response = modelMapper.map(record, HealthCheckRecordResponse.class);
                    response.setRecordId(record.getRecordId());
                    response.setStudentId(record.getStudent().getAccountId());
                    response.setNurseId(record.getNurse().getAccountId());
                    response.setCheckNoticeId(record.getHealthCheckNotice().getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckRecordResponse> getRecordsByNotice(Long checkNoticeId) {
        List<HealthCheckRecord> records = healthCheckRecordRepository.findByHealthCheckNotice_CheckNoticeId(checkNoticeId);
        return records.stream()
                .map(record -> {
                    HealthCheckRecordResponse response = modelMapper.map(record, HealthCheckRecordResponse.class);
                    response.setRecordId(record.getRecordId());
                    response.setStudentId(record.getStudent().getAccountId());
                    response.setNurseId(record.getNurse().getAccountId());
                    response.setCheckNoticeId(record.getHealthCheckNotice().getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckRecordResponse> getRecordsByDate(LocalDate date) {
        List<HealthCheckRecord> records = healthCheckRecordRepository.findByDate(date);
        return records.stream()
                .map(record -> {
                    HealthCheckRecordResponse response = modelMapper.map(record, HealthCheckRecordResponse.class);
                    response.setRecordId(record.getRecordId());
                    response.setStudentId(record.getStudent().getAccountId());
                    response.setNurseId(record.getNurse().getAccountId());
                    response.setCheckNoticeId(record.getHealthCheckNotice().getCheckNoticeId());
                    return response;
                })
                .collect(Collectors.toList());
    }


    @Override
    public HealthCheckRecordResponse updateRecord(Long recordId, HealthCheckRecordRequest request) {
        HealthCheckRecord record = healthCheckRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Health check record not found with id: " + recordId));
        
        // Update fields from request
        record.setResults(request.getResult());
        if (request.getDate() != null) {
            record.setDate(LocalDate.parse(request.getDate()));
        }
        // Update health check notice if provided
        if (request.getHealthCheckNoticeId() != null) {
            HealthCheckNotice notice = healthCheckNoticeRepository.findById(request.getHealthCheckNoticeId())
                    .orElseThrow(() -> new RuntimeException("Health check notice not found with id: " + request.getHealthCheckNoticeId()));
            record.setHealthCheckNotice(notice);
        }
        // Save updated record
        HealthCheckRecord updatedRecord = healthCheckRecordRepository.save(record);

        // Map to response
        HealthCheckRecordResponse response = modelMapper.map(updatedRecord, HealthCheckRecordResponse.class);
        response.setRecordId(updatedRecord.getRecordId());
        response.setStudentId(updatedRecord.getStudent().getAccountId());
        response.setNurseId(updatedRecord.getNurse().getAccountId());
        response.setCheckNoticeId(updatedRecord.getHealthCheckNotice().getCheckNoticeId());
        response.setResults(updatedRecord.getResults());
        response.setDate(updatedRecord.getDate());
        return response;
    }

    @Override
    public void deleteRecord(Long recordId) {
        if (!healthCheckRecordRepository.existsById(recordId)) {
            throw new RuntimeException("Health check record not found with id: " + recordId);
        }
        healthCheckRecordRepository.deleteById(recordId);
    }
} 