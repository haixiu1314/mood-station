package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderRefund {
    private Long id;
    private Long orderId;
    private Long userId;
    private String orderNo;
    private Integer totalAmount;
    private String reason;
    private Integer status; // 0待审核 1通过 2不通过
    private LocalDateTime applyTime;
    private LocalDateTime reviewTime;
    private Long reviewerId;
}
