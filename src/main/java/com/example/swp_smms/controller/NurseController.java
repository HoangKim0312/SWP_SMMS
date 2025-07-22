package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.response.ChildData;
import com.example.swp_smms.service.NurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nurse")
@RequiredArgsConstructor
public class NurseController {

    private final NurseService nurseService;

    @GetMapping("/students/ongoing-medication")
    public List<ChildData> getStudentsWithOngoingMedication() {
        return nurseService.getAllStudentsWithOngoingMedicationSent();
    }


}
