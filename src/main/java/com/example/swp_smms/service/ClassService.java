package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.ClassRequest;
import com.example.swp_smms.model.payload.response.ClassResponse;

import java.util.List;

public interface ClassService {
    ClassResponse createClass(ClassRequest request);
    List<ClassResponse> getClassesByGradeInCurrentYear(String grade);

}
