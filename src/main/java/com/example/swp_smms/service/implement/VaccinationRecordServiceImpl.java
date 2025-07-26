package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.payload.request.VaccinationRecordRequest;
import com.example.swp_smms.model.payload.response.VaccinationRecordResponse;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.VaccinationRecordService;
import com.example.swp_smms.service.VaccineBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationRecordServiceImpl implements VaccinationRecordService {

    private final VaccinationRecordRepository recordRepository;
    private final AccountRepository accountRepository;
    private final VaccinationNoticeRepository noticeRepository;
    private final VaccineBatchRepository vaccineBatchRepository;
    private final MedicalProfileRepository medicalProfileRepository;

    @Override
    public VaccinationRecordResponse createRecord(VaccinationRecordRequest request, UUID nurseId) {
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        Account nurse = accountRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + nurseId));

        VaccinationNotice notice = noticeRepository.findById(request.getVaccineNoticeId())
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + request.getVaccineNoticeId()));
        VaccineBatch batch = notice.getVaccineBatch();
        if (batch == null || batch.getQuantity() <= 0) {
            throw new RuntimeException("Vaccine batch is unavailable.");
        }
        batch.setQuantity(batch.getQuantity() - 1);
        vaccineBatchRepository.save(batch);


        MedicalProfile medicalProfile = medicalProfileRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Medical profile not found for student."));

        VaccinationRecord record = new VaccinationRecord();
        record.setStudent(student);
        record.setNurse(nurse);
        record.setVaccinationNotice(notice);
        record.setResults(request.getResults());
        record.setReaction(request.getReaction());
        record.setDoseNumber(request.getDoseNumber());
        record.setNote(request.getNote());
        record.setDate(request.getDate());
        VaccinationRecord saved = recordRepository.save(record);
        return mapToResponse(saved);
    }


    @Override
    public VaccinationRecordResponse getRecordById(Long id) {
        VaccinationRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination record not found with id: " + id));
        return mapToResponse(record);
    }

    @Override
    public List<VaccinationRecordResponse> getAllRecords() {
        return recordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationRecordResponse> getRecordsByStudent(UUID studentId) {
        return recordRepository.findByStudent_AccountId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationRecordResponse> getRecordsByNurse(UUID nurseId) {
        return recordRepository.findByNurse_AccountId(nurseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationRecordResponse> getRecordsByNotice(Long vaccineNoticeId) {
        return recordRepository.findByVaccinationNotice_VaccineNoticeId(vaccineNoticeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VaccinationRecordResponse updateRecord(Long id, VaccinationRecordRequest request) {
        VaccinationRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination record not found with id: " + id));

        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        VaccinationNotice notice = noticeRepository.findById(request.getVaccineNoticeId())
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + request.getVaccineNoticeId()));

        record.setStudent(student);
        record.setVaccinationNotice(notice);
        record.setResults(request.getResults());
        record.setDate(request.getDate());

        VaccinationRecord updatedRecord = recordRepository.save(record);
        return mapToResponse(updatedRecord);
    }

    @Override
    public void deleteRecord(Long id) {
        if (!recordRepository.existsById(id)) {
            throw new RuntimeException("Vaccination record not found with id: " + id);
        }
        recordRepository.deleteById(id);
    }

    private VaccinationRecordResponse mapToResponse(VaccinationRecord record) {
        return VaccinationRecordResponse.builder()
                .recordId(record.getRecordId())
                .studentId(record.getStudent().getAccountId())
                .studentName(record.getStudent().getFullName())
                .nurseId(record.getNurse().getAccountId())
                .nurseName(record.getNurse().getFullName())
                .vaccineNoticeId(record.getVaccinationNotice().getVaccineNoticeId())
                .vaccineName(record.getVaccinationNotice().getVaccineBatch().getVaccine().getName())
                .results(record.getResults())
                .reaction(record.getReaction())
                .doseNumber(record.getDoseNumber())
                .note(record.getNote())
                .date(record.getDate())
                .build();
    }

} 