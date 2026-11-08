package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CoinLog {
    private Long id;
    private Long userId;
    private Integer changeAmount;
    private Integer balanceAfter;
    private String type;
    private String reason;
    private Long operatorId;
    private Long relatedId;
    private LocalDateTime createTime;
}
