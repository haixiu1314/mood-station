package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单视图对象，用于封装订单相关的数据
 */
@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private Integer totalAmount;
    private String status;
    private String statusText;
    private String addressName;
    private String addressPhone;
    private String addressDetail;
    private LocalDateTime createTime;
    private LocalDateTime shippedTime;
    private LocalDateTime refundedTime;
    // 快递信息
    private String expressCompany;
    private String expressNo;
    private String expressStatus;
    private LocalDateTime receivedTime;
    private LocalDateTime confirmedTime;
    // 发货人信息
    private String senderName;
    private String senderAddress;
    private String senderPhone;
    // 退款信息
    private String refundReason;
    private Integer refundStatus;
    private List<OrderItem> items;
}
