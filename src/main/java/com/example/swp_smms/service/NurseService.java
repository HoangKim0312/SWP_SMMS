package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Account;

import java.util.List;

public interface NurseService {
    List<Account> getAllStudentsWithOngoingMedicationSent();
}
