package com.moodlater.service;

import com.moodlater.entity.Diary;
import com.moodlater.entity.DiaryTrash;
import com.moodlater.mapper.DiaryMapper;
import com.moodlater.mapper.DiaryTrashMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private DiaryTrashMapper diaryTrashMapper;

    @Autowired
    private DailyRewardService dailyRewardService;

    public Map<String, Object> createDiary(Diary diary) {
        diaryMapper.insert(diary);
        Diary created = diaryMapper.selectById(diary.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("diary", created);
        
        // 每日首次发布日记奖励
        int rewardAmount = dailyRewardService.grantReward(diary.getUserId(), "daily_diary", 2);
        if (rewardAmount > 0) {
            result.put("rewardMessage", "日记发布成功，获得" + rewardAmount + "个心动币");
            result.put("rewardAmount", rewardAmount);
        }
        
        return result;
    }

    public Diary getDiaryById(Long id) {
        return diaryMapper.selectById(id);
    }

    public List<Diary> getUserDiaries(Long userId) {
        return diaryMapper.selectByUserId(userId);
    }

    public List<Diary> getRecentDiaries(Long userId, Integer limit) {
        return diaryMapper.selectRecentByUserId(userId, limit);
    }

    public void updateDiary(Diary diary) {
        diaryMapper.updateById(diary);
    }

    @Transactional
    public void deleteDiary(Long id) {
        Diary diary = diaryMapper.selectById(id);
        if (diary == null) {
            return;
        }

        int trashCount = diaryTrashMapper.countByUserId(diary.getUserId());
        if (trashCount >= 10) {
            throw new RuntimeException("回收站已满，最多存储10份删除的日记");
        }

        DiaryTrash trash = new DiaryTrash();
        trash.setUserId(diary.getUserId());
        trash.setDiaryId(diary.getId());
        trash.setEmotion(diary.getEmotion());
        trash.setEmotionLabel(diary.getEmotionLabel());
        trash.setTitle(diary.getTitle());
        trash.setContent(diary.getContent());
        trash.setImages(diary.getImages());
        trash.setIsPublic(diary.getIsPublic());
        trash.setOriginalCreateTime(diary.getCreateTime());
        diaryTrashMapper.insert(trash);

        diaryMapper.softDelete(id);
    }

    public List<DiaryTrash> getTrashDiaries(Long userId) {
        return diaryTrashMapper.selectByUserId(userId);
    }

    @Transactional
    public void restoreDiary(Long trashId, Long userId) {
        DiaryTrash trash = diaryTrashMapper.selectById(trashId);
        if (trash == null || !trash.getUserId().equals(userId)) {
            throw new RuntimeException("回收记录不存在");
        }
        diaryMapper.restore(trash.getDiaryId());
        diaryTrashMapper.deleteById(trashId, userId);
    }

    @Transactional
    public void permanentlyDelete(Long trashId, Long userId) {
        DiaryTrash trash = diaryTrashMapper.selectById(trashId);
        if (trash == null || !trash.getUserId().equals(userId)) {
            throw new RuntimeException("回收记录不存在");
        }
        diaryMapper.deleteById(trash.getDiaryId());
        diaryTrashMapper.deleteById(trashId, userId);
    }

    public List<Diary> getDiariesByDate(Long userId, String date) {
        return diaryMapper.selectByUserIdAndDate(userId, date);
    }

    public List<Diary> searchDiaries(Long userId, String keyword) {
        return diaryMapper.searchByUserIdAndKeyword(userId, keyword);
    }

    public List<Diary> getPublicDiaries(Long userId) {
        return diaryMapper.selectPublicByUserId(userId);
    }
}
