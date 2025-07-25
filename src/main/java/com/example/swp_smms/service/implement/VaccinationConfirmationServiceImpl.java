package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.StudentParent;
import com.example.swp_smms.model.entity.VaccinationConfirmation;
import com.example.swp_smms.model.entity.VaccinationNotice;
import com.example.swp_smms.model.payload.request.VaccinationConfirmationRequest;
import com.example.swp_smms.model.payload.request.VaccinationConfirmationStatusRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.NoticeStatisticalResponse;
import com.example.swp_smms.model.payload.response.VaccinationConfirmationResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.StudentParentRepository;
import com.example.swp_smms.repository.VaccinationConfirmationRepository;
import com.example.swp_smms.repository.VaccinationNoticeRepository;
import com.example.swp_smms.service.VaccinationConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationConfirmationServiceImpl implements VaccinationConfirmationService {

    private final VaccinationConfirmationRepository confirmationRepository;
    private final AccountRepository accountRepository;
    private final VaccinationNoticeRepository noticeRepository;
    private final StudentParentRepository studentParentRepository;

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
    public VaccinationConfirmationResponse updateConfirmation(VaccinationConfirmationRequest request) {
        VaccinationConfirmation confirmation = confirmationRepository.findById(request.getVaccinationConfirmationId())
                .orElseThrow(() -> new RuntimeException("Vaccination confirmation not found with id: " + request.getVaccinationConfirmationId()));

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

    @Override
    public List<VaccinationConfirmationResponse> getConfirmationsByStatusAndParentId(String status, UUID parentId) {
        return confirmationRepository.findByStatusAndParentLinkedStudents(status, parentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



    @Override
    public List<AccountResponse> getConfirmedStudentsByNotice(Long vaccineNoticeId) {
        List<VaccinationConfirmation> confirmations = confirmationRepository
                .findByVaccinationNotice_VaccineNoticeId(vaccineNoticeId);

        return confirmations.stream()
                .filter(c -> "CONFIRMED".equalsIgnoreCase(c.getStatus()))
                .map(VaccinationConfirmation::getStudent)
                .distinct()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse mapToAccountResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getAccountId());
        response.setUsername(account.getUsername());
        response.setFullName(account.getFullName());
        response.setDob(account.getDob());
        response.setGender(account.getGender());
        response.setPhone(account.getPhone());
        response.setRoleId(account.getRole().getRoleId());
        response.setEmail(account.getEmail());
        response.setEmailNotificationsEnabled(account.getEmailNotificationsEnabled());
        response.setNotificationTypes(account.getNotificationTypes());
        return response;
    }

    @Override
    public List<VaccinationConfirmationResponse> confirmAllUpcomingNoticesForAllChildren(UUID parentId) {
        Account parent = accountRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        List<StudentParent> studentLinks = studentParentRepository.findByParent_AccountId(parentId);
        List<VaccinationConfirmationResponse> confirmedList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (StudentParent link : studentLinks) {
            Account student = link.getStudent();
            int grade = student.getClazz().getGrade();

            List<VaccinationNotice> upcomingNotices = noticeRepository.findByVaccinationDateAfter(today).stream()
                    .filter(notice -> notice.getGrade() == grade)
                    .toList();

            List<Long> alreadyConfirmedNoticeIds = confirmationRepository.findByStudent_AccountId(student.getAccountId()).stream()
                    .map(c -> c.getVaccinationNotice().getVaccineNoticeId())
                    .toList();

            for (VaccinationNotice notice : upcomingNotices) {
                if (!alreadyConfirmedNoticeIds.contains(notice.getVaccineNoticeId())) {
                    VaccinationConfirmation confirmation = new VaccinationConfirmation();
                    confirmation.setVaccinationNotice(notice);
                    confirmation.setStudent(student);
                    confirmation.setParent(parent);
                    confirmation.setStatus("CONFIRMED");
                    confirmation.setConfirmedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                    VaccinationConfirmation saved = confirmationRepository.save(confirmation);
                    confirmedList.add(mapToResponse(saved));
                }
            }
        }

        return confirmedList;
    }

    @Transactional
    @Override
    public int confirmAllPendingByParent(UUID parentId) {
        return confirmationRepository.confirmAllPendingByParent(parentId,LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Override
    public VaccinationConfirmationResponse updateStatusOnly(VaccinationConfirmationStatusRequest request) {
        VaccinationConfirmation confirmation = confirmationRepository.findById(request.getVaccinationConfirmationId())
                .orElseThrow(() -> new RuntimeException("Vaccination confirmation not found with id: " + request.getVaccinationConfirmationId()));

        String status = request.getStatus().toUpperCase();

        // Optional: validate allowed status
        List<String> allowedStatuses = List.of("CONFIRMED", "DECLINED", "COMPLETED", "ONGOING");
        if (!allowedStatuses.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        confirmation.setStatus(status);

        // Set confirmedAt if confirmed or declined
        if (status.equals("CONFIRMED") || status.equals("DECLINED")) {
            confirmation.setConfirmedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        VaccinationConfirmation updated = confirmationRepository.save(confirmation);
        return mapToResponse(updated);
    }

    @Override
    public NoticeStatisticalResponse getStatusCountsByNoticeId(Long noticeId) {
        List<Object[]> statusCounts = confirmationRepository.countByStatusForNotice(noticeId);

        long confirmed = 0, pending = 0, declined = 0, completed = 0, ongoing = 0;

        for (Object[] row : statusCounts) {
            String status = (String) row[0];
            Long count = (Long) row[1];

            switch (status.toUpperCase()) {
                case "CONFIRMED" -> confirmed = count;
                case "PENDING" -> pending = count;
                case "DECLINED" -> declined = count;
                case "COMPLETED" -> completed = count;
                case "ONGOING" -> ongoing = count;
            }
        }

        return NoticeStatisticalResponse.builder()
                .noticeId(noticeId)
                .confirmedCount(confirmed)
                .pendingCount(pending)
                .declinedCount(declined)
                .completedCount(completed)
                .ongoingCount(ongoing)
                .build();
    }




    private VaccinationConfirmationResponse mapToResponse(VaccinationConfirmation confirmation) {
        Account parent = confirmation.getParent();
        return VaccinationConfirmationResponse.builder()
                .confirmationId(confirmation.getConfirmationId())
                .vaccineNoticeId(confirmation.getVaccinationNotice().getVaccineNoticeId())
                .vaccineName(confirmation.getVaccinationNotice().getVaccineName())
                .studentId(confirmation.getStudent().getAccountId())
                .studentName(confirmation.getStudent().getFullName())
                .parentId(parent != null ? parent.getAccountId() : null)
                .parentName(parent != null ? parent.getFullName() : null)
                .status(confirmation.getStatus())
                .confirmedAt(confirmation.getConfirmedAt())
                .build();
    }

} 