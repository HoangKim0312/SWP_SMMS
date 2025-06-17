package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.VaccinationConfirmation;
import com.example.swp_smms.model.entity.VaccinationNotice;
import com.example.swp_smms.model.payload.request.VaccinationConfirmationRequest;
import com.example.swp_smms.model.payload.response.VaccinationConfirmationResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.VaccinationConfirmationRepository;
import com.example.swp_smms.repository.VaccinationNoticeRepository;
import com.example.swp_smms.service.VaccinationConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationConfirmationServiceImpl implements VaccinationConfirmationService {

    private final VaccinationConfirmationRepository confirmationRepository;
    private final AccountRepository accountRepository;
    private final VaccinationNoticeRepository noticeRepository;

    @Override
    public VaccinationConfirmationResponse createConfirmation(VaccinationConfirmationRequest request) {
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + request.getParentId()));

        VaccinationNotice notice = noticeRepository.findById(request.getVaccineNoticeId())
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + request.getVaccineNoticeId()));

        VaccinationConfirmation confirmation = new VaccinationConfirmation();
        confirmation.setVaccinationNotice(notice);
        confirmation.setStudent(student);
        confirmation.setParent(parent);
        confirmation.setStatus(request.getStatus());
        confirmation.setConfirmedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        VaccinationConfirmation savedConfirmation = confirmationRepository.save(confirmation);
        return mapToResponse(savedConfirmation);
    }

    @Override
    public VaccinationConfirmationResponse getConfirmationById(Long id) {
        VaccinationConfirmation confirmation = confirmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination confirmation not found with id: " + id));
        return mapToResponse(confirmation);
    }

    @Override
    public List<VaccinationConfirmationResponse> getAllConfirmations() {
        return confirmationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationConfirmationResponse> getConfirmationsByStudent(UUID studentId) {
        return confirmationRepository.findByStudent_AccountId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationConfirmationResponse> getConfirmationsByParent(UUID parentId) {
        return confirmationRepository.findByParent_AccountId(parentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationConfirmationResponse> getConfirmationsByNotice(Long vaccineNoticeId) {
        return confirmationRepository.findByVaccinationNotice_VaccineNoticeId(vaccineNoticeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationConfirmationResponse> getConfirmationsByStatus(String status) {
        return confirmationRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VaccinationConfirmationResponse updateConfirmation(Long id, VaccinationConfirmationRequest request) {
        VaccinationConfirmation confirmation = confirmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination confirmation not found with id: " + id));

        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + request.getParentId()));

        VaccinationNotice notice = noticeRepository.findById(request.getVaccineNoticeId())
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + request.getVaccineNoticeId()));

        confirmation.setVaccinationNotice(notice);
        confirmation.setStudent(student);
        confirmation.setParent(parent);
        confirmation.setStatus(request.getStatus());
        confirmation.setConfirmedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        VaccinationConfirmation updatedConfirmation = confirmationRepository.save(confirmation);
        return mapToResponse(updatedConfirmation);
    }

    @Override
    public void deleteConfirmation(Long id) {
        if (!confirmationRepository.existsById(id)) {
            throw new RuntimeException("Vaccination confirmation not found with id: " + id);
        }
        confirmationRepository.deleteById(id);
    }

    private VaccinationConfirmationResponse mapToResponse(VaccinationConfirmation confirmation) {
        return VaccinationConfirmationResponse.builder()
                .confirmationId(confirmation.getConfirmationId())
                .vaccineNoticeId(confirmation.getVaccinationNotice().getVaccineNoticeId())
                .vaccineName(confirmation.getVaccinationNotice().getVaccineName())
                .studentId(confirmation.getStudent().getAccountId())
                .studentName(confirmation.getStudent().getFullName())
                .parentId(confirmation.getParent().getAccountId())
                .parentName(confirmation.getParent().getFullName())
                .status(confirmation.getStatus())
                .confirmedAt(confirmation.getConfirmedAt())
                .build();
    }
} 