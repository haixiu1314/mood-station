package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StockLog {
    private Long id;
    private Long productId;
    private Integer changeAmount;
    private Integer stockAfter;
    private Long operatorId;
    private String reason;
    private LocalDateTime createTime;
}
