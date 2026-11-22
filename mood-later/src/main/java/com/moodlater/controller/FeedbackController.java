package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 提交用户反馈
     */
    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        String content = params.get("content") == null ? "" : params.get("content").toString();
        if (content.trim().isEmpty()) {
            return Result.error("反馈内容不能为空");
        }
        if (content.length() > 1000) {
            return Result.error("反馈内容不能超过1000个字符");
        }
        feedbackService.submit(userId, content.trim());
        return Result.success();
    }
}
