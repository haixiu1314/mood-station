package com.moodlater.entity;

import lombok.Data;

@Data
public class DashboardVO {
    private Long orderCount;
    private Long orderQuantity;
    private Long orderAmount;
    private Long refundCount;
    private Long completedOrders;
    private Long completedAmount;
    private Long refundedOrders;
    private Long refundedAmount;
    private Long pendingOrders;
}
