package com.moodlater.mapper;

import com.moodlater.entity.DailyReward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;

@Mapper
public interface DailyRewardMapper {
    DailyReward selectByUserAndType(@Param("userId") Long userId, @Param("rewardType") String rewardType, @Param("rewardDate") LocalDate rewardDate);
    int insert(DailyReward reward);
}
