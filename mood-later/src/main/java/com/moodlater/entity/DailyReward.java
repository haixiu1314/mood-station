package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyReward {
    private Long id;
    private Long userId;
    private String rewardType;
    private LocalDate rewardDate;
    private Integer amount;
}
