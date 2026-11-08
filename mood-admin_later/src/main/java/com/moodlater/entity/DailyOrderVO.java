package com.moodlater.entity;

import lombok.Data;

@Data
public class DailyOrderVO {
    private String date;
    private Long orderCount;
    private Long orderAmount;
    private Long completedCount;
    private Long completedAmount;
    private Long refundedCount;
    private Long refundedAmount;
    private Long pendingCount;
}
