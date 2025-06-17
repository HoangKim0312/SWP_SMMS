package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.MedicalProfile;
import com.example.swp_smms.model.payload.request.MedicalProfileRequest;
import com.example.swp_smms.model.payload.response.ListMedicalProfileResponse;
import com.example.swp_smms.model.payload.response.MedicalProfileResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.MedicalProfileRepository;
import com.example.swp_smms.repository.RoleRepository;
import com.example.swp_smms.service.MedicalProfileService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public MedicalProfileResponse createMedicalProfile(UUID studentId, Long recordID, MedicalProfileRequest request) {

        if (!accountRepository.existsByAccountIdAndRole_RoleId(studentId, 1L)) {
            throw new RuntimeException("Student not exists or not a student");
        }

        //NOT AVAILABLE, NEED FUTURE UPDATE:
        //check if recordID is valid and studentID(record) = studentID(medicalProfile)


        // Map request to entity
        MedicalProfile profile = modelMapper.map(request, MedicalProfile.class);

        // Set student entity
        Account student = accountRepository.findAccountByAccountId(studentId);
        profile.setStudent(student);

        // Set recordId
        profile.setRecordId(recordID);

        // Save to DB
        medicalProfileRepository.save(profile);

        // Map to response and manually set studentId if not auto-mapped
        MedicalProfileResponse response = modelMapper.map(profile, MedicalProfileResponse.class);
        response.setStudentId(studentId);  // make sure MedicalProfileResponse has this field
        return response;
    }

    @Override
    public MedicalProfileResponse getLastMedicalProfile(UUID studentId) {
        Pageable pageable = PageRequest.of(0, 1); // Only get the latest one
        List<MedicalProfile> latestProfiles = medicalProfileRepository.findMedicalProfilesByStudentId(studentId, pageable);

        if (latestProfiles.isEmpty()) {
            return null; // or throw a custom NotFoundException
        }

        MedicalProfile profile = latestProfiles.get(0);

        // Map entity to response
        MedicalProfileResponse response = modelMapper.map(profile, MedicalProfileResponse.class);
        return response;
    }


    @Override
    public ListMedicalProfileResponse getAllMedicalProfiles(UUID studentId) {

        List<MedicalProfile> profiles = medicalProfileRepository.findMedicalProfilesByStudentId(studentId);

        List<MedicalProfileResponse> responseList = profiles.stream()
                .map(profile -> modelMapper.map(profile, MedicalProfileResponse.class))
                .toList();

        ListMedicalProfileResponse response = new ListMedicalProfileResponse();
        response.setMedicalProfiles(responseList);
        return response;
    }

    @Override
    @Transactional
    public void deleteMedicalProfile(UUID studentId, Long medicalProfileId) {
        boolean exists = medicalProfileRepository.existsById(medicalProfileId);
        if (!exists) {
            throw new RuntimeException("Medical profile not found with ID: " + medicalProfileId);
        }

        // Optional: Check that the profile belongs to the student
        MedicalProfile profile = medicalProfileRepository.findById(medicalProfileId)
                .orElseThrow(() -> new RuntimeException("Medical profile not found"));

        if (!profile.getStudent().getAccountId().equals(studentId)) {
            throw new RuntimeException("This profile does not belong to the specified student.");
        }

        medicalProfileRepository.deleteByStudentIdAndMedicalProfileId(studentId, medicalProfileId);
    }


}
