package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.DiseaseResponse;
import com.example.swp_smms.model.payload.response.NoticeStatisticalResponse;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;
import com.example.swp_smms.repository.*;
import com.example.swp_smms.service.VaccinationNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final DiseaseRepository diseaseRepository;
    private final VaccinationNoticeExcludeDiseaseRepository vaccinationNoticeExcludeDiseaseRepository;
    private VaccinationNoticeResponse mapToResponse(VaccinationNotice notice, long confirmationCount,List<Long> excludedDiseaseIds) {
        return VaccinationNoticeResponse.builder()
                .vaccineNoticeId(notice.getVaccineNoticeId())
                .title(notice.getTitle())
                .description(notice.getDescription())
                .vaccineName(notice.getVaccineName())
                .vaccinationDate(notice.getVaccinationDate())
                .createdAt(notice.getCreatedAt())
                .grade(notice.getGrade())
                .vaccineBatchId(notice.getVaccineBatch().getVaccineBatchId())
                .totalStudentsSentForm(confirmationCount)
                .excludedDiseaseIds(excludedDiseaseIds)
                .build();
    }

    @Override
    @Transactional
    public VaccinationNoticeResponse createNotice(VaccinationNoticeRequest request) {
        VaccineBatch batch = vaccineBatchRepository.findById(request.getVaccineBatchId())
                .orElseThrow(() -> new RuntimeException("Vaccine batch not found with ID: " + request.getVaccineBatchId()));

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

        //Step 2: Link excluded diseases
        List<Disease> excludedDiseases = diseaseRepository.findAllById(request.getExcludeDiseaseIds());
        List<VaccineNoticeExcludeDisease> exclusions = excludedDiseases.stream()
                .map(disease -> new VaccineNoticeExcludeDisease(null, saved, disease))
                .toList();
        vaccinationNoticeExcludeDiseaseRepository.saveAll(exclusions);

        // Get excluded disease IDs
        List<Long> excludedDiseaseIds = excludedDiseases.stream()
                .map(Disease::getDiseaseId)
                .toList();

        // Step 3: Get eligible students (filtered by grade and active disease exclusions)
        List<Account> eligibleStudents = accountRepository.findEligibleStudentsForNotice(
                request.getGrade(), excludedDiseaseIds);

        // Step 4: Create confirmation form for all suitable students
        List<VaccinationConfirmation> confirmations = eligibleStudents.stream()
                .map(student -> {
                    VaccinationConfirmation confirmation = new VaccinationConfirmation();
                    confirmation.setVaccinationNotice(saved);
                    confirmation.setStudent(student);
                    confirmation.setStatus("PENDING");
                    confirmation.setParent(null);
                    confirmation.setConfirmedAt(null);
                    return confirmation;
                })
                .toList();

        vaccinationConfirmationRepository.saveAll(confirmations);
        long confirmationCount = confirmations.size();

        return mapToResponse(saved,confirmationCount,excludedDiseaseIds);
    }



    @Override
    public VaccinationNoticeResponse getNoticeById(Long id) {
        VaccinationNotice notice = vaccinationNoticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + id));
        List<Long> excludedDiseaseIds = notice.getExcludedDiseases().stream()
                .map(disease -> disease.getDisease().getDiseaseId())
                .collect(Collectors.toList());
        long confirmationCount = vaccinationConfirmationRepository.countByVaccinationNoticeId(id);
        return mapToResponse(notice,confirmationCount,excludedDiseaseIds);
    }

    @Override
    public List<VaccinationNoticeResponse> getAllNotices() {
        return vaccinationNoticeRepository.findAll().stream()
                .map(notice -> {
                    long confirmationCount = vaccinationConfirmationRepository.countByVaccinationNoticeId(notice.getVaccineNoticeId());

                    List<Long> excludedDiseaseIds = notice.getExcludedDiseases().stream()
                            .map(disease -> disease.getDisease().getDiseaseId())
                            .collect(Collectors.toList());

                    return mapToResponse(notice, confirmationCount,excludedDiseaseIds);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationNoticeResponse> searchNoticesByVaccineName(String vaccineName) {
        return vaccinationNoticeRepository.findByVaccineNameContainingIgnoreCase(vaccineName).stream()
                .map(notice -> {
                    long confirmationCount = vaccinationConfirmationRepository.countByVaccinationNoticeId(notice.getVaccineNoticeId());
                    List<Long> excludedDiseaseIds = notice.getExcludedDiseases().stream()
                            .map(disease -> disease.getDisease().getDiseaseId())
                            .collect(Collectors.toList());
                    return mapToResponse(notice, confirmationCount,excludedDiseaseIds);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationNoticeResponse> getNoticesForToday() {
        LocalDate today = LocalDate.now();
        return vaccinationNoticeRepository.findByVaccinationDate(today).stream()
                .map(notice -> {
                    long confirmationCount = vaccinationConfirmationRepository.countByVaccinationNoticeId(notice.getVaccineNoticeId());
                    List<Long> excludedDiseaseIds = notice.getExcludedDiseases().stream()
                            .map(disease -> disease.getDisease().getDiseaseId())
                            .collect(Collectors.toList());
                    return mapToResponse(notice, confirmationCount,excludedDiseaseIds);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationNoticeResponse> getActiveNotices() {
        LocalDate today = LocalDate.now();
        return vaccinationNoticeRepository.findByVaccinationDateAfter(today).stream()
                .map(notice -> {
                    List<Long> excludedDiseaseIds = notice.getExcludedDiseases().stream()
                            .map(disease -> disease.getDisease().getDiseaseId())
                            .collect(Collectors.toList());
                    long confirmationCount = vaccinationConfirmationRepository
                            .countByVaccinationNotice_VaccineNoticeId(notice.getVaccineNoticeId());
                    return mapToResponse(notice, confirmationCount,excludedDiseaseIds);
                })
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
                .map(notice -> {
                    long confirmationCount = vaccinationConfirmationRepository
                            .countByVaccinationNotice_VaccineNoticeId(notice.getVaccineNoticeId());
                    List<Long> excludedDiseaseIds = notice.getExcludedDiseases().stream()
                            .map(disease -> disease.getDisease().getDiseaseId())
                            .collect(Collectors.toList());
                    return mapToResponse(notice, confirmationCount,excludedDiseaseIds);
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<VaccinationNoticeResponse> filterNotices(Long vaccineId, Long vaccineBatchId, LocalDate vaccinationDate, boolean exact) {
        return vaccinationNoticeRepository.findAll().stream()
                .filter(notice -> {
                    if (vaccineId != null && !vaccineId.equals(notice.getVaccineBatch().getVaccine().getVaccineId())) {
                        return false;
                    }
                    if (vaccineBatchId != null && !vaccineBatchId.equals(notice.getVaccineBatch().getVaccineBatchId())) {
                        return false;
                    }
                    if (vaccinationDate != null) {
                        if (exact && !notice.getVaccinationDate().isEqual(vaccinationDate)) {
                            return false;
                        }
                        if (!exact && notice.getVaccinationDate().isBefore(vaccinationDate)) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(notice -> {
                    long confirmationCount = vaccinationConfirmationRepository
                            .countByVaccinationNotice_VaccineNoticeId(notice.getVaccineNoticeId());
                    List<Long> excludedDiseaseIds = notice.getExcludedDiseases().stream()
                            .map(disease -> disease.getDisease().getDiseaseId())
                            .collect(Collectors.toList());
                    return mapToResponse(notice, confirmationCount,excludedDiseaseIds);
                })
                .collect(Collectors.toList());
    }


} 