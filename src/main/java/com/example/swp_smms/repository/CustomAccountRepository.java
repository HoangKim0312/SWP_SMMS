package com.example.swp_smms.repository;



import com.example.swp_smms.model.payload.request.AccountFilterRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import org.springframework.data.domain.Page;
public interface CustomAccountRepository {
    Page<AccountResponse> filterStudents(AccountFilterRequest filter);
}
