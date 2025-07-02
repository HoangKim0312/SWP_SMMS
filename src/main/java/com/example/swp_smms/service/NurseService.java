package com.example.swp_smms.service;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.model.payload.response.ChildData;

import java.util.List;

public interface NurseService {
    List<ChildData> getAllStudentsWithOngoingMedicationSent();
}
