package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
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
    public ListMedicationSentResponse getAllMedicationSentsForStudent(UUID studentId) {
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


}
