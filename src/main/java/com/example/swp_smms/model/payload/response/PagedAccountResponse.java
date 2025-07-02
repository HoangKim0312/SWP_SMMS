package com.example.swp_smms.model.payload.response;

import lombok.Data;
import java.util.List;

@Data
public class PagedAccountResponse {
    private List<AccountResponse> accounts;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
