package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.User;
import com.moodlater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        String nickname = params.getOrDefault("nickname", "");
        String avatarUrl = params.getOrDefault("avatarUrl", "");
        Map<String, Object> result = userService.loginOrRegister(code, nickname, avatarUrl);
        return Result.success(result);
    }

    @PostMapping("/login/account")
    public Result<Map<String, Object>> loginByAccount(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || username.trim().isEmpty()) {
            return Result.error("账号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        Map<String, Object> result = userService.loginByUsername(username.trim(), password);
        if (result == null) {
            return Result.error("账号或密码错误");
        }
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String nickname = params.getOrDefault("nickname", "");
        String avatarUrl = params.getOrDefault("avatarUrl", "");
        String phone = params.getOrDefault("phone", null);
        String genderStr = params.get("gender");
        Integer gender = genderStr != null ? Integer.parseInt(genderStr) : 0;

        if (username == null || username.trim().isEmpty()) {
            return Result.error("账号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            return Result.error("用户名只能包含英文字母和数字");
        }
        if (username.length() > 8) {
            return Result.error("账号不能超过8位");
        }
        if (password.length() > 8) {
            return Result.error("密码不能超过8位");
        }
        if (nickname == null || nickname.trim().isEmpty()) {
            return Result.error("昵称不能为空");
        }

        Map<String, Object> result = userService.registerWithAccount(
            username.trim(), password, nickname.trim(), avatarUrl, phone, gender
        );
        if (result.containsKey("errorType")) {
            String errorType = (String) result.get("errorType");
            if ("username".equals(errorType)) {
                return Result.error("账号已存在，请更换其他账号");
            } else if ("nickname".equals(errorType)) {
                return Result.error("昵称已被使用，请更换其他昵称");
            }
            return Result.error("注册失败");
        }
        return Result.success(result);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestAttribute("userId") Long userId) {
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    /**
     * 获取指定用户的基本信息（公开）
     */
    @GetMapping("/{userId}")
    public Result<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserInfo(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 只返回公开信息
        User publicUser = new User();
        publicUser.setId(user.getId());
        publicUser.setNickname(user.getNickname());
        publicUser.setAvatarUrl(user.getAvatarUrl());
        publicUser.setBio(user.getBio());
        return Result.success(publicUser);
    }

    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody User user, @RequestAttribute("userId") Long userId) {
        user.setId(userId);
        userService.updateUserInfo(user);
        return Result.success();
    }

    @GetMapping("/balance")
    public Result<Integer> getCoinBalance(@RequestAttribute("userId") Long userId) {
        User user = userService.getUserInfo(userId);
        return Result.success(user.getCoinBalance());
    }

    @PutMapping("/nickname")
    public Result<Void> updateNickname(@RequestBody Map<String, String> params, @RequestAttribute("userId") Long userId) {
        String nickname = params.get("nickname");
        if (nickname == null || nickname.trim().isEmpty()) {
            return Result.error("昵称不能为空");
        }
        if (nickname.length() > 16) {
            return Result.error("昵称最多16个字符");
        }
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname.trim());
        userService.updateUserInfo(user);
        return Result.success();
    }

    @PutMapping("/avatar")
    public Result<Void> updateAvatar(@RequestBody Map<String, String> params, @RequestAttribute("userId") Long userId) {
        String avatarUrl = params.get("avatarUrl");
        User user = new User();
        user.setId(userId);
        user.setAvatarUrl(avatarUrl);
        userService.updateUserInfo(user);
        return Result.success();
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody Map<String, String> params, @RequestAttribute("userId") Long userId) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            return Result.error("原密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error("新密码不能为空");
        }
        if (newPassword.length() > 8) {
            return Result.error("新密码不能超过8位");
        }
        boolean success = userService.changePassword(userId, oldPassword, newPassword);
        if (!success) {
            return Result.error("原密码错误");
        }
        return Result.success();
    }
}
