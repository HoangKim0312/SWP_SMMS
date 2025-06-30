package com.example.swp_smms.service;

import java.util.UUID;
import java.util.List;
import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.response.StudentSummaryResponse;

public interface StudentParentService {
    void linkStudentParent(UUID studentId, UUID parentId);
    void unlinkStudentParent(UUID studentId, UUID parentId);
    List<StudentSummaryResponse> getChildrenForParent(UUID parentId);
} 