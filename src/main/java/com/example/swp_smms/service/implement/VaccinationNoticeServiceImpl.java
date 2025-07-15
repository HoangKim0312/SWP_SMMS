package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.VaccinationNotice;
import com.example.swp_smms.model.entity.VaccineBatch;
import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;
import com.example.swp_smms.repository.VaccinationNoticeRepository;
import com.example.swp_smms.repository.VaccineBatchRepository;
import com.example.swp_smms.service.VaccinationNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationNoticeServiceImpl implements VaccinationNoticeService {

    private final VaccinationNoticeRepository vaccinationNoticeRepository;
    private final VaccineBatchRepository vaccineBatchRepository;

    @Override
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

        VaccinationNotice notice = new VaccinationNotice();
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        notice.setVaccineName(batch.getVaccine().getName());
        notice.setVaccinationDate(request.getVaccinationDate());
        notice.setCreatedAt(today);
        notice.setVaccineBatch(batch);
        notice.setGrade(request.getGrade());

        VaccinationNotice saved = vaccinationNoticeRepository.save(notice);
        return mapToResponse(saved); // ✅ Now return response
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


} 