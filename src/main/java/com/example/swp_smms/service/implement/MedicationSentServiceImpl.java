package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.payload.request.DosageRequest;
import com.example.swp_smms.model.payload.request.MedicationItemRequest;
import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.*;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.MedicalProfileRepository;
import com.example.swp_smms.repository.MedicationSentRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.MedicationSentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicationSentServiceImpl implements MedicationSentService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MedicationSentRepository medicationSentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListMedicationSentResponse getAllActiveMedicationSentsForStudent(UUID studentId) {
        String today = LocalDate.now().toString();
        // Fetch all medication sent records for the student
        List<MedicationSent> sentList = medicationSentRepository.findActiveMedicationsByStudentIdAndDate(studentId,today);

        // Map each MedicationSent entity to MedicationSentResponse
        List<MedicationSentResponse> responseList = sentList.stream()
                .map(entity -> {
                    MedicationSentResponse response = modelMapper.map(entity, MedicationSentResponse.class);
                    response.setStudentId(entity.getStudent().getAccountId());
                    response.setParentId(entity.getParent().getAccountId());
                    return response;
                })
                .toList();

        // Wrap in response object
        ListMedicationSentResponse result = new ListMedicationSentResponse();
        result.setMedicationSentList(responseList);
        return result;
    }

    @Override
    public ListMedicationSentResponse getAllMedicationSentsForStudent(UUID studentId) {
        String today = LocalDate.now().toString();
        // Fetch all medication sent records for the student
        List<MedicationSent> sentList = medicationSentRepository.findAllByStudentId(studentId);

        // Map each MedicationSent entity to MedicationSentResponse
        List<MedicationSentResponse> responseList = sentList.stream()
                .map(entity -> {
                    MedicationSentResponse response = modelMapper.map(entity, MedicationSentResponse.class);
                    response.setStudentId(entity.getStudent().getAccountId());
                    response.setParentId(entity.getParent().getAccountId());
                    return response;
                })
                .toList();

        // Wrap in response object
        ListMedicationSentResponse result = new ListMedicationSentResponse();
        result.setMedicationSentList(responseList);
        return result;
    }

    @Override
    public void deleteMedicationSent(UUID studentId, Long medicationSentId) {
        MedicationSent medicationSent = medicationSentRepository.findById(medicationSentId)
                .orElseThrow(() -> new RuntimeException("MedicationSent not found with ID: " + medicationSentId));

        // Validate student ownership
        if (!medicationSent.getStudent().getAccountId().equals(studentId)) {
            throw new RuntimeException("This MedicationSent does not belong to the specified student.");
        }

        // Soft delete: mark as inactive
        medicationSent.setActive(false);
        medicationSentRepository.save(medicationSent);
    }


    @Override
    public ListMedicationSentResponse getAllActiveMedicationSentsForAllStudents() {
        String today = LocalDate.now().toString();
        List<MedicationSent> sentList = medicationSentRepository.findAllActiveMedications(today);

        List<MedicationSentResponse> responseList = sentList.stream()
                .map(entity -> {
                    MedicationSentResponse response = modelMapper.map(entity, MedicationSentResponse.class);
                    response.setStudentId(entity.getStudent().getAccountId());
                    response.setParentId(entity.getParent().getAccountId());
                    return response;
                })
                .toList();

        ListMedicationSentResponse result = new ListMedicationSentResponse();
        result.setMedicationSentList(responseList);
        return result;
    }

    // MedicationSentServiceImpl.java

    @Override
    public MedicationSentResponse createMedicationSent(UUID studentId, UUID parentId, MedicationSentRequest request) {
        String sentAt = LocalDate.now().toString();

        // Validate parent-child relationship
        List<ChildData> children = accountRepository.findChildrenAccounts(parentId);
        boolean isChild = children.stream()
                .anyMatch(child -> child.getChildId().equals(studentId));
        if (!isChild) {
            throw new RuntimeException("This student does not belong to the specified parent.");
        }

        // Fetch student and parent accounts
        Account student = accountRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Account parent = accountRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        // Build MedicationSent entity
        MedicationSent medSent = new MedicationSent();
        medSent.setStudent(student);
        medSent.setParent(parent);
        medSent.setSentAt(sentAt);
        medSent.setRequestDate(request.getRequestDate());

        List<Dosage> dosageList = new ArrayList<>();
        for (DosageRequest dosageRequest : request.getDosages()) {
            Dosage dosage = new Dosage();
            dosage.setTimingNotes(dosageRequest.getTimingNotes());
            dosage.setMedicationSent(medSent);

            List<MedicationItem> itemList = new ArrayList<>();
            for (MedicationItemRequest itemRequest : dosageRequest.getMedicationItems()) {
                MedicationItem item = new MedicationItem();
                item.setDosage(dosage);
                item.setMedicationName(itemRequest.getMedicationName());
                item.setAmount(itemRequest.getAmount());
                itemList.add(item);
            }

            dosage.setMedicationItems(itemList);
            dosageList.add(dosage);
        }

        medSent.setDosages(dosageList);
        MedicationSent saved = medicationSentRepository.save(medSent);

        // ✅ Manually map to response DTO
        MedicationSentResponse response = new MedicationSentResponse();
        response.setMedSentId(saved.getMedSentId());
        response.setStudentId(saved.getStudent().getAccountId());
        response.setParentId(saved.getParent().getAccountId());
        response.setRequestDate(saved.getRequestDate());
        response.setSentAt(saved.getSentAt());

        List<DosageResponse> dosageResponses = saved.getDosages().stream().map(dosage -> {
            DosageResponse dr = new DosageResponse();
            dr.setTimingNotes(dosage.getTimingNotes());

            List<MedicationItemResponse> itemResponses = dosage.getMedicationItems().stream().map(item -> {
                MedicationItemResponse ir = new MedicationItemResponse();
                ir.setMedicationName(item.getMedicationName());
                ir.setAmount(item.getAmount());
                return ir;
            }).toList();

            dr.setMedicationItems(itemResponses);
            return dr;
        }).toList();

        response.setDosages(dosageResponses);
        return response;
    }

    @Override
    public MedicationSentResponse updateMedicationSent(UUID studentId, Long medicationSentId, MedicationSentRequest request) {
        MedicationSent existing = medicationSentRepository.findById(medicationSentId)
                .orElseThrow(() -> new RuntimeException("MedicationSent not found with ID: " + medicationSentId));

        if (!existing.getStudent().getAccountId().equals(studentId)) {
            throw new RuntimeException("This MedicationSent does not belong to the specified student.");
        }

        existing.getDosages().clear();

        List<Dosage> dosageList = new ArrayList<>();
        for (DosageRequest dosageRequest : request.getDosages()) {
            Dosage dosage = new Dosage();
            dosage.setTimingNotes(dosageRequest.getTimingNotes());
            dosage.setMedicationSent(existing);

            List<MedicationItem> itemList = new ArrayList<>();
            for (MedicationItemRequest itemRequest : dosageRequest.getMedicationItems()) {
                MedicationItem item = new MedicationItem();
                item.setDosage(dosage);
                item.setMedicationName(itemRequest.getMedicationName());
                item.setAmount(itemRequest.getAmount());
                itemList.add(item);
            }
            dosage.setMedicationItems(itemList);
            dosageList.add(dosage);
        }
        existing.setRequestDate(request.getRequestDate());
        existing.setDosages(dosageList);
        MedicationSent saved = medicationSentRepository.save(existing);

        return modelMapper.map(saved, MedicationSentResponse.class);
    }

    @Override
    public MedicationSentResponse getMedicationSentById(UUID studentId, Long medicationSentId) {
        MedicationSent entity = medicationSentRepository.findById(medicationSentId)
                .orElseThrow(() -> new RuntimeException("MedicationSent not found"));

        // Ensure it belongs to the student and is active
        if (!entity.getStudent().getAccountId().equals(studentId)) {
            throw new RuntimeException("This MedicationSent does not belong to the specified student.");
        }

        if (!entity.isActive()) {
            throw new RuntimeException("This MedicationSent is inactive.");
        }

        MedicationSentResponse response = new MedicationSentResponse();
        response.setMedSentId(entity.getMedSentId());
        response.setStudentId(entity.getStudent().getAccountId());
        response.setParentId(entity.getParent().getAccountId());
        response.setRequestDate(entity.getRequestDate());
        response.setSentAt(entity.getSentAt());

        List<DosageResponse> dosageResponses = entity.getDosages().stream().map(dosage -> {
            DosageResponse dr = new DosageResponse();
            dr.setTimingNotes(dosage.getTimingNotes());

            List<MedicationItemResponse> itemResponses = dosage.getMedicationItems().stream().map(item -> {
                MedicationItemResponse ir = new MedicationItemResponse();
                ir.setMedicationName(item.getMedicationName());
                ir.setAmount(item.getAmount());
                return ir;
            }).toList();

            dr.setMedicationItems(itemResponses);
            return dr;
        }).toList();

        response.setDosages(dosageResponses);
        return response;
    }


}
