package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.MedicationSent;
import com.example.swp_smms.model.entity.StudentParent;
import com.example.swp_smms.model.payload.request.MedicationSentRequest;
import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.model.payload.response.ListMedicationSentResponse;
import com.example.swp_smms.model.payload.response.MedicationSentResponse;
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
    public MedicationSentResponse createMedicationSent(UUID studentId, UUID parentId, MedicationSentRequest request) {

        String sentAt = LocalDate.now().toString();
        List<ChildData> children = accountRepository.findChildrenAccounts(parentId);
        boolean isChild = children.stream()
                .anyMatch(child -> child.getChildId().equals(studentId));

        if (!isChild) {
            throw new RuntimeException("This student does not belong to the specified parent.");
        }

        Account student = accountRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Account parent = accountRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        MedicationSent entity = modelMapper.map(request, MedicationSent.class);
        entity.setStudent(student);
        entity.setParent(parent);
        entity.setSentAt(sentAt);

        MedicationSent saved = medicationSentRepository.save(entity);

        MedicationSentResponse response = modelMapper.map(saved, MedicationSentResponse.class);
        response.setStudentId(student.getAccountId());
        response.setParentId(parent.getAccountId());
        response.setSentAt(sentAt);

        return response;
    }

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
        boolean exists = medicationSentRepository.existsById(medicationSentId);
        if (!exists) {
            throw new RuntimeException("MedicalSent not found with ID: " + medicationSentId);
        }

        // Optional: Check that the profile belongs to the student
        MedicationSent medicationSent = medicationSentRepository.findById(medicationSentId)
                .orElseThrow(() -> new RuntimeException("MedicalSent not found"));

        if (!medicationSent.getStudent().getAccountId().equals(studentId)) {
            throw new RuntimeException("This medSent does not belong to the specified student.");
        }

        medicationSentRepository.deleteByStudentIdAndMedicationSentId(studentId, medicationSentId);
    }

    @Override
    public MedicationSentResponse updateMedicationSent(UUID studentId, Long medicationSentId, MedicationSentRequest request) {
        // Retrieve the existing record
        MedicationSent existing = medicationSentRepository.findById(medicationSentId)
                .orElseThrow(() -> new RuntimeException("MedicationSent not found with ID: " + medicationSentId));

        // Check ownership
        if (!existing.getStudent().getAccountId().equals(studentId)) {
            throw new RuntimeException("This MedicationSent does not belong to the specified student.");
        }

        // Map updated fields from request to the existing entity
        modelMapper.map(request, existing);

        // Save changes
        MedicationSent updated = medicationSentRepository.save(existing);

        // Map to response
        MedicationSentResponse response = modelMapper.map(updated, MedicationSentResponse.class);
        response.setStudentId(updated.getStudent().getAccountId());
        response.setParentId(updated.getParent().getAccountId());

        return response;
    }


}
