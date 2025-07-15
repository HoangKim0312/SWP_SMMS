package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.VaccinationNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationNoticeServiceImpl implements VaccinationNoticeService {

    private final VaccinationNoticeRepository vaccinationNoticeRepository;
    private final VaccineBatchRepository vaccineBatchRepository;
    private final StudentParentRepository studentParentRepository;
    private final AccountRepository accountRepository;
    private final VaccinationConfirmationRepository vaccinationConfirmationRepository;


    @Override
    @Transactional
    public VaccinationNoticeResponse createNotice(VaccinationNoticeRequest request, Long vaccineBatchId) {
        VaccineBatch batch = vaccineBatchRepository.findById(vaccineBatchId)
                .orElseThrow(() -> new RuntimeException("Vaccine batch not found with ID: " + vaccineBatchId));

        LocalDate vaccinationDate = request.getVaccinationDate();
        LocalDate expiryDate = batch.getExpiryDate();
        LocalDate today = LocalDate.now();

        if (!vaccinationDate.isAfter(today)) {
            throw new RuntimeException("Vaccination date must be after today.");
        }

        if (!vaccinationDate.isBefore(expiryDate)) {
            throw new RuntimeException("Vaccination date must be before vaccine batch expiry date.");
        }

        // Step 1: Create the notice
        VaccinationNotice notice = new VaccinationNotice();
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        notice.setVaccineName(batch.getVaccine().getName());
        notice.setVaccinationDate(vaccinationDate);
        notice.setCreatedAt(today);
        notice.setVaccineBatch(batch);
        notice.setGrade(request.getGrade());

        VaccinationNotice saved = vaccinationNoticeRepository.save(notice);

        // Step 2: Find all students in the specified grade
        List<Account> students = accountRepository.findAll().stream()
                .filter(account -> account.getRole().getRoleId() == 1) // assuming roleId 1 = Student
                .filter(account -> account.getClazz() != null && account.getClazz().getGrade() == request.getGrade())
                .collect(Collectors.toList());

        // Step 3: For each student, find their parent and insert VaccinationConfirmation
        for (Account student : students) {
            List<StudentParent> links = student.getStudentParents(); // students mapped to parents

            for (StudentParent sp : links) {
                VaccinationConfirmation confirmation = new VaccinationConfirmation();
                confirmation.setVaccinationNotice(saved);
                confirmation.setStudent(student);
                confirmation.setParent(sp.getParent());
                confirmation.setStatus("PENDING");
                confirmation.setConfirmedAt(null);
                vaccinationConfirmationRepository.save(confirmation);
            }
        }

        return mapToResponse(saved);
    }



    @Override
    public VaccinationNoticeResponse getNoticeById(Long id) {
        VaccinationNotice notice = vaccinationNoticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + id));
        return mapToResponse(notice);
    }

    @Override
    public List<VaccinationNoticeResponse> getAllNotices() {
        return vaccinationNoticeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationNoticeResponse> searchNoticesByVaccineName(String vaccineName) {
        return vaccinationNoticeRepository.findByVaccineNameContainingIgnoreCase(vaccineName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VaccinationNoticeResponse updateNotice(Long id, VaccinationNoticeRequest request) {
        VaccinationNotice notice = vaccinationNoticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + id));

        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        notice.setVaccineName(notice.getVaccineName());
        notice.setVaccinationDate(request.getVaccinationDate());
        notice.setCreatedAt(LocalDate.now());
        notice.setGrade(request.getGrade());

        VaccinationNotice updatedNotice = vaccinationNoticeRepository.save(notice);
        return mapToResponse(updatedNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        if (!vaccinationNoticeRepository.existsById(id)) {
            throw new RuntimeException("Vaccination notice not found with id: " + id);
        }
        vaccinationNoticeRepository.deleteById(id);
    }

    private VaccinationNoticeResponse mapToResponse(VaccinationNotice notice) {
        return VaccinationNoticeResponse.builder()
                .vaccineNoticeId(notice.getVaccineNoticeId())
                .title(notice.getTitle())
                .description(notice.getDescription())
                .vaccineName(notice.getVaccineName())
                .vaccinationDate(notice.getVaccinationDate()) // renamed
                .createdAt(notice.getCreatedAt())
                .grade(notice.getGrade())
                .build();
    }

    @Override
    public List<VaccinationNoticeResponse> getNoticesForToday() {
        LocalDate today = LocalDate.now();
        return vaccinationNoticeRepository.findByVaccinationDate(today).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationNoticeResponse> getActiveNotices() {
        LocalDate today = LocalDate.now();
        return vaccinationNoticeRepository.findByVaccinationDateAfter(today).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationNoticeResponse> getActiveNoticesByParent(UUID parentId) {
        List<Integer> childGrades = studentParentRepository.findChildrenGradesByParentId(parentId);
        if (childGrades.isEmpty()) {
            return List.of(); // No children → return empty list
        }

        LocalDate today = LocalDate.now();
        List<VaccinationNotice> notices = vaccinationNoticeRepository
                .findByGradeInAndVaccinationDateAfter(childGrades, today);

        return notices.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


} 