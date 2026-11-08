package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Comment;
import com.moodlater.entity.Post;
import com.moodlater.entity.PostAppeal;
import com.moodlater.service.AdminPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/post")
public class AdminPostController {

    @Autowired
    private AdminPostService postService;

    @GetMapping("/list")
    public Result<List<Post>> listPosts() {
        return Result.success(postService.listAllPosts());
    }

    @GetMapping("/{id}")
    public Result<Post> getPostById(@PathVariable Long id) {
        return Result.success(postService.getPostById(id));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return Result.success();
    }

    @PostMapping("/batch-delete")
    public Result<Void> batchDeletePosts(@RequestBody Map<String, List<Long>> params) {
        postService.batchDeletePosts(params.get("ids"));
        return Result.success();
    }

    @GetMapping("/comment/list")
    public Result<List<Comment>> listComments() {
        return Result.success(postService.listAllComments());
    }

    @GetMapping("/comment/{id}")
    public Result<Comment> getCommentById(@PathVariable Long id) {
        return Result.success(postService.getCommentById(id));
    }

    @DeleteMapping("/comment/delete/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        postService.deleteComment(id);
        return Result.success();
    }

    @PostMapping("/comment/batch-delete")
    public Result<Void> batchDeleteComments(@RequestBody Map<String, List<Long>> params) {
        postService.batchDeleteComments(params.get("ids"));
        return Result.success();
    }

    // 申诉管理
    @GetMapping("/appeal/list")
    public Result<List<PostAppeal>> listPendingAppeals() {
        return Result.success(postService.listPendingAppeals());
    }

    @GetMapping("/appeal/{id}")
    public Result<PostAppeal> getAppealById(@PathVariable Long id) {
        return Result.success(postService.getAppealById(id));
    }

    @PostMapping("/appeal/review")
    public Result<Void> reviewAppeal(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        Long reviewerId = params.get("reviewerId") != null ? Long.valueOf(params.get("reviewerId").toString()) : 1L;
        postService.reviewAppeal(id, status, reviewerId);
        return Result.success();
    }

    @GetMapping("/appeal/count")
    public Result<Integer> countPendingAppeals() {
        return Result.success(postService.countPendingAppeals());
    }
}
