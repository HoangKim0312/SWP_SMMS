package com.example.swp_smms.service;

import com.example.swp_smms.model.payload.request.ClassRequest;
import com.example.swp_smms.model.payload.response.ClassResponse;

public interface ClassService {
    ClassResponse createClass(ClassRequest request);
}
