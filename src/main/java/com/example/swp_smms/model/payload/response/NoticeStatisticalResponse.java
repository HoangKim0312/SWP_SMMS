package com.example.swp_smms.model.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeStatisticalResponse {
    private Long noticeId;
    private long confirmedCount;
    private long pendingCount;
    private long declinedCount;
    private long completedCount;
    private long ongoingCount;
}
