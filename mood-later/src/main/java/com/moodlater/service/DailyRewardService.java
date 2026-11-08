package com.moodlater.service;

import com.moodlater.entity.DailyReward;
import com.moodlater.mapper.DailyRewardMapper;
import com.moodlater.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DailyRewardService {

    @Autowired
    private DailyRewardMapper dailyRewardMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 发放每日奖励
     * @param userId 用户ID
     * @param rewardType 奖励类型
     * @param amount 奖励金额
     * @return 实际获得的金额（0表示今天已领取过）
     */
    @Transactional
    public int grantReward(Long userId, String rewardType, int amount) {
        LocalDate today = LocalDate.now();
        DailyReward existing = dailyRewardMapper.selectByUserAndType(userId, rewardType, today);
        if (existing != null) {
            return 0;
        }
        DailyReward reward = new DailyReward();
        reward.setUserId(userId);
        reward.setRewardType(rewardType);
        reward.setRewardDate(today);
        reward.setAmount(amount);
        dailyRewardMapper.insert(reward);
        userMapper.addBalance(userId, amount);
        return amount;
    }
}
