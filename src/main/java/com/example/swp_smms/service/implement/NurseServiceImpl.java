package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.service.NurseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class NurseServiceImpl implements NurseService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ChildData> getAllStudentsWithOngoingMedicationSent() {
        LocalDate today = LocalDate.now();
        List<ChildData> students = accountRepository.findStudentsWithOngoingMedication(today);
        return students;
    }
}
