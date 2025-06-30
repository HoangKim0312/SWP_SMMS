package com.example.swp_smms.service;

import java.util.UUID;

public interface StudentParentService {
    void linkStudentParent(UUID studentId, UUID parentId);
    void unlinkStudentParent(UUID studentId, UUID parentId);
} 