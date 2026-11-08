package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Diary;
import com.moodlater.entity.DiaryTrash;
import com.moodlater.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @PostMapping
    public Result<Map<String, Object>> createDiary(@RequestBody Diary diary, @RequestAttribute("userId") Long userId) {
        if (diary.getTitle() == null || diary.getTitle().trim().isEmpty()) {
            return Result.error("标题不能为空");
        }
        if (diary.getTitle().length() > 18) {
            return Result.error("标题长度不能超过18个字符");
        }
        if (diary.getContent() == null || diary.getContent().trim().isEmpty()) {
            return Result.error("内容不能为空");
        }
        if (diary.getContent().length() > 5000) {
            return Result.error("内容长度不能超过5000个字符");
        }
        diary.setUserId(userId);
        Map<String, Object> result = diaryService.createDiary(diary);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Diary> getDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);
        return Result.success(diary);
    }

    @GetMapping("/list")
    public Result<List<Diary>> getUserDiaries(@RequestAttribute("userId") Long userId) {
        List<Diary> diaries = diaryService.getUserDiaries(userId);
        return Result.success(diaries);
    }

    @GetMapping("/recent")
    public Result<List<Diary>> getRecentDiaries(@RequestAttribute("userId") Long userId,
                                                 @RequestParam(defaultValue = "5") Integer limit) {
        List<Diary> diaries = diaryService.getRecentDiaries(userId, limit);
        return Result.success(diaries);
    }

    @PutMapping("/{id}")
    public Result<Void> updateDiary(@PathVariable Long id, @RequestBody Diary diary) {
        if (diary.getTitle() == null || diary.getTitle().trim().isEmpty()) {
            return Result.error("标题不能为空");
        }
        if (diary.getTitle().length() > 18) {
            return Result.error("标题长度不能超过18个字符");
        }
        if (diary.getContent() != null && diary.getContent().length() > 5000) {
            return Result.error("内容长度不能超过5000个字符");
        }
        diary.setId(id);
        diaryService.updateDiary(diary);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDiary(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        try {
            diaryService.deleteDiary(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/trash")
    public Result<List<DiaryTrash>> getTrashDiaries(@RequestAttribute("userId") Long userId) {
        List<DiaryTrash> trashList = diaryService.getTrashDiaries(userId);
        return Result.success(trashList);
    }

    @PostMapping("/trash/{trashId}/restore")
    public Result<Void> restoreDiary(@PathVariable Long trashId, @RequestAttribute("userId") Long userId) {
        try {
            diaryService.restoreDiary(trashId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/trash/{trashId}")
    public Result<Void> permanentlyDelete(@PathVariable Long trashId, @RequestAttribute("userId") Long userId) {
        try {
            diaryService.permanentlyDelete(trashId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public Result<List<Diary>> filterByDate(@RequestAttribute("userId") Long userId,
                                             @RequestParam String date) {
        List<Diary> diaries = diaryService.getDiariesByDate(userId, date);
        return Result.success(diaries);
    }

    @GetMapping("/search")
    public Result<List<Diary>> searchDiaries(@RequestAttribute("userId") Long userId,
                                              @RequestParam String keyword) {
        List<Diary> diaries = diaryService.searchDiaries(userId, keyword);
        return Result.success(diaries);
    }

    /**
     * 获取用户公开日记（他人可见）
     */
    @GetMapping("/public/{userId}")
    public Result<List<Diary>> getPublicDiaries(@PathVariable Long userId) {
        List<Diary> diaries = diaryService.getPublicDiaries(userId);
        return Result.success(diaries);
    }
}
