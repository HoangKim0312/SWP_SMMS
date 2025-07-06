package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.HealthCheckConfirmation;
import com.example.swp_smms.model.entity.HealthCheckNotice;
import com.example.swp_smms.model.payload.request.HealthCheckConfirmationRequest;
import com.example.swp_smms.model.payload.response.HealthCheckConfirmationResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.HealthCheckConfirmationRepository;
import com.example.swp_smms.repository.HealthCheckNoticeRepository;
import com.example.swp_smms.service.HealthCheckConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthCheckConfirmationServiceImpl implements HealthCheckConfirmationService {

    private final HealthCheckConfirmationRepository confirmationRepository;
    private final AccountRepository accountRepository;
    private final HealthCheckNoticeRepository noticeRepository;

    @Override
    public HealthCheckConfirmationResponse createConfirmation(HealthCheckConfirmationRequest request) {
        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + request.getParentId()));

        HealthCheckNotice notice = noticeRepository.findById(request.getHealthCheckNoticeId())
                .orElseThrow(() -> new RuntimeException("Health check notice not found with id: " + request.getHealthCheckNoticeId()));

        HealthCheckConfirmation confirmation = new HealthCheckConfirmation();
        confirmation.setHealthCheckNotice(notice);
        confirmation.setStudent(student);
        confirmation.setParent(parent);
        confirmation.setStatus(request.getStatus());
        confirmation.setConfirmedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        HealthCheckConfirmation savedConfirmation = confirmationRepository.save(confirmation);
        return mapToResponse(savedConfirmation);
    }

    @Override
    public HealthCheckConfirmationResponse getConfirmationById(Long id) {
        HealthCheckConfirmation confirmation = confirmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health check confirmation not found with id: " + id));
        return mapToResponse(confirmation);
    }

    @Override
    public List<HealthCheckConfirmationResponse> getAllConfirmations() {
        return confirmationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckConfirmationResponse> getConfirmationsByStudent(UUID studentId) {
        return confirmationRepository.findByStudent_AccountId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckConfirmationResponse> getConfirmationsByParent(UUID parentId) {
        return confirmationRepository.findByParent_AccountId(parentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckConfirmationResponse> getConfirmationsByNotice(Long checkNoticeId) {
        return confirmationRepository.findByHealthCheckNotice_CheckNoticeId(checkNoticeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckConfirmationResponse> getConfirmationsByStatus(String status) {
        return confirmationRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckConfirmationResponse> getConfirmationsByDate(String confirmedAt) {
        return confirmationRepository.findByConfirmedAt(confirmedAt).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCheckConfirmationResponse> getConfirmationsByTitle(String title) {
        return confirmationRepository.findByHealthCheckNotice_TitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HealthCheckConfirmationResponse updateConfirmation(Long id, HealthCheckConfirmationRequest request) {
        HealthCheckConfirmation confirmation = confirmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health check confirmation not found with id: " + id));

        Account student = accountRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        Account parent = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + request.getParentId()));

        HealthCheckNotice notice = noticeRepository.findById(request.getHealthCheckNoticeId())
                .orElseThrow(() -> new RuntimeException("Health check notice not found with id: " + request.getHealthCheckNoticeId()));

        confirmation.setHealthCheckNotice(notice);
        confirmation.setStudent(student);
        confirmation.setParent(parent);
        confirmation.setStatus(request.getStatus());
        confirmation.setConfirmedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        HealthCheckConfirmation updatedConfirmation = confirmationRepository.save(confirmation);
        return mapToResponse(updatedConfirmation);
    }

    @Override
    public void deleteConfirmation(Long id) {
        if (!confirmationRepository.existsById(id)) {
            throw new RuntimeException("Health check confirmation not found with id: " + id);
        }
        confirmationRepository.deleteById(id);
    }

    private HealthCheckConfirmationResponse mapToResponse(HealthCheckConfirmation confirmation) {
        return HealthCheckConfirmationResponse.builder()
                .confirmationId(confirmation.getConfirmationId())
                .checkNoticeId(confirmation.getHealthCheckNotice().getCheckNoticeId())
                .studentId(confirmation.getStudent().getAccountId())
                .parentId(confirmation.getParent().getAccountId())
                .status(confirmation.getStatus())
                .confirmedAt(confirmation.getConfirmedAt())
                .build();
    }
} 