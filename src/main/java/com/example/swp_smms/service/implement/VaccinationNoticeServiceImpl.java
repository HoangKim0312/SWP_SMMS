package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.VaccinationNotice;
import com.example.swp_smms.model.payload.request.VaccinationNoticeRequest;
import com.example.swp_smms.model.payload.response.VaccinationNoticeResponse;
import com.example.swp_smms.repository.VaccinationNoticeRepository;
import com.example.swp_smms.service.VaccinationNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationNoticeServiceImpl implements VaccinationNoticeService {

    private final VaccinationNoticeRepository noticeRepository;

    @Override
    public VaccinationNoticeResponse createNotice(VaccinationNoticeRequest request) {
        VaccinationNotice notice = new VaccinationNotice();
        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        notice.setVaccineName(request.getVaccineName());
        notice.setDate(request.getDate());
        notice.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        VaccinationNotice savedNotice = noticeRepository.save(notice);
        return mapToResponse(savedNotice);
    }

    @Override
    public VaccinationNoticeResponse getNoticeById(Long id) {
        VaccinationNotice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + id));
        return mapToResponse(notice);
    }

    @Override
    public List<VaccinationNoticeResponse> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationNoticeResponse> searchNoticesByVaccineName(String vaccineName) {
        return noticeRepository.findByVaccineNameContainingIgnoreCase(vaccineName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VaccinationNoticeResponse updateNotice(Long id, VaccinationNoticeRequest request) {
        VaccinationNotice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination notice not found with id: " + id));

        notice.setTitle(request.getTitle());
        notice.setDescription(request.getDescription());
        notice.setVaccineName(request.getVaccineName());
        notice.setDate(request.getDate());

        VaccinationNotice updatedNotice = noticeRepository.save(notice);
        return mapToResponse(updatedNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new RuntimeException("Vaccination notice not found with id: " + id);
        }
        noticeRepository.deleteById(id);
    }

    private VaccinationNoticeResponse mapToResponse(VaccinationNotice notice) {
        return VaccinationNoticeResponse.builder()
                .vaccineNoticeId(notice.getVaccineNoticeId())
                .title(notice.getTitle())
                .description(notice.getDescription())
                .vaccineName(notice.getVaccineName())
                .date(notice.getDate())
                .createdAt(notice.getCreatedAt())
                .build();
    }
} 