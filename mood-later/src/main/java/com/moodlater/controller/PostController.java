package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Comment;
import com.moodlater.entity.Post;
import com.moodlater.entity.PostAppeal;
import com.moodlater.entity.PostTrash;
import com.moodlater.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Result<Map<String, Object>> createPost(@RequestBody Post post, @RequestAttribute("userId") Long userId) {
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            return Result.error("标题不能为空");
        }
        if (post.getTitle().length() > 18) {
            return Result.error("标题长度不能超过18个字符");
        }
        if (post.getEmotion() == null || post.getEmotion().trim().isEmpty()) {
            return Result.error("请选择情绪类型");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            return Result.error("内容不能为空");
        }
        if (post.getContent().length() > 5000) {
            return Result.error("内容长度不能超过5000个字符");
        }
        post.setUserId(userId);
        Map<String, Object> result = postService.createPost(post);
        return Result.success(result);
    }

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getAllPosts(@RequestAttribute(value = "userId", required = false) Long userId) {
        List<Map<String, Object>> posts = postService.getAllPosts(userId);
        return Result.success(posts);
    }

    @GetMapping("/my")
    public Result<List<Post>> getMyPosts(@RequestAttribute("userId") Long userId) {
        List<Post> posts = postService.getUserPosts(userId);
        return Result.success(posts);
    }

    @GetMapping("/filter")
    public Result<List<Post>> filterPosts(@RequestAttribute("userId") Long userId, @RequestParam String date) {
        List<Post> posts = postService.getPostsByDate(userId, date);
        return Result.success(posts);
    }

    @GetMapping("/search")
    public Result<List<Post>> searchPosts(@RequestAttribute("userId") Long userId, @RequestParam String keyword) {
        List<Post> posts = postService.searchPosts(userId, keyword);
        return Result.success(posts);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getPost(@PathVariable Long id, @RequestAttribute(value = "userId", required = false) Long userId) {
        Map<String, Object> post = postService.getPostById(id, userId);
        return Result.success(post);
    }

    @PostMapping("/like/{postId}")
    public Result<Map<String, Object>> likePost(@PathVariable Long postId, @RequestAttribute("userId") Long userId) {
        Map<String, Object> result = postService.likePost(userId, postId);
        return Result.success(result);
    }

    @DeleteMapping("/like/{postId}")
    public Result<Void> unlikePost(@PathVariable Long postId, @RequestAttribute("userId") Long userId) {
        postService.unlikePost(userId, postId);
        return Result.success();
    }

    @PostMapping("/comment")
    public Result<Map<String, Object>> addComment(@RequestBody Comment comment, @RequestAttribute("userId") Long userId) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }
        if (comment.getContent().length() > 300) {
            return Result.error("评论内容不能超过300个字符");
        }
        comment.setUserId(userId);
        Map<String, Object> result = postService.addComment(comment);
        return Result.success(result);
    }

    @GetMapping("/comment/{postId}")
    public Result<List<Map<String, Object>>> getComments(@PathVariable Long postId) {
        List<Map<String, Object>> comments = postService.getCommentsByPostId(postId);
        return Result.success(comments);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePost(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        try {
            postService.deletePost(id, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/trash")
    public Result<List<PostTrash>> getTrashPosts(@RequestAttribute("userId") Long userId) {
        List<PostTrash> trashList = postService.getTrashPosts(userId);
        return Result.success(trashList);
    }

    @PostMapping("/trash/{trashId}/restore")
    public Result<Void> restorePost(@PathVariable Long trashId, @RequestAttribute("userId") Long userId) {
        try {
            postService.restorePost(trashId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/trash/{trashId}")
    public Result<Void> permanentlyDeletePost(@PathVariable Long trashId, @RequestAttribute("userId") Long userId) {
        try {
            postService.permanentlyDeletePost(trashId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/appeal")
    public Result<Void> submitAppeal(@RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        Long postId = Long.valueOf(params.get("postId").toString());
        String reason = params.get("reason").toString();
        if (reason == null || reason.trim().isEmpty()) {
            return Result.error("申诉原因不能为空");
        }
        try {
            postService.submitAppeal(userId, postId, reason);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/appeal")
    public Result<List<PostAppeal>> getMyAppeals(@RequestAttribute("userId") Long userId) {
        List<PostAppeal> appeals = postService.getUserAppeals(userId);
        return Result.success(appeals);
    }
}
