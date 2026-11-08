package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Integer totalAmount;
    private String status;
    private String addressName;
    private String addressPhone;
    private String addressDetail;
    private LocalDateTime createTime;
    private LocalDateTime shippedTime;
    private LocalDateTime refundedTime;
    // 快递相关
    private String expressCompany;
    private String expressNo;
    private String expressCourier;
    private String expressStatus; // delivering/signed
    private LocalDateTime receivedTime;
    private LocalDateTime confirmedTime;
    // 发货人信息
    private String senderName;
    private String senderAddress;
    private String senderPhone;
}
