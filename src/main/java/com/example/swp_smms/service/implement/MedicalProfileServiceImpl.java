package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.entity.Role;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.MedicalProfileRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.MedicalProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MedicalProfileServiceImpl implements MedicalProfileService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MedicalProfileRepository medicalProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MedicalProfileResponse createMedicalProfile(UUID studentId, MedicalProfileRequest request) {

        if (!accountRepository.existsByAccountIdAndRole_RoleId(studentId, 3L)) {
            throw new RuntimeException("Student not exists or not a student");
        }

        if (medicalProfileRepository.existsByStudent_AccountId(studentId)) {
            throw new RuntimeException("Medical profile already exists for this student");
        }

        // Map request to entity
        MedicalProfile profile = modelMapper.map(request, MedicalProfile.class);

        // Set student entity
        Account student = accountRepository.findAccountByAccountId(studentId);
        profile.setStudent(student);

        // Save to DB
        medicalProfileRepository.save(profile);

        // Map to response and manually set studentId if not auto-mapped
        MedicalProfileResponse response = modelMapper.map(profile, MedicalProfileResponse.class);
        response.setStudentId(studentId);  // make sure MedicalProfileResponse has this field
        return response;
    }


}
