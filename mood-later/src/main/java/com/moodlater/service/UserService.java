package com.moodlater.service;

import com.moodlater.entity.User;
import com.moodlater.mapper.UserMapper;
import com.moodlater.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DailyRewardService dailyRewardService;

    public Map<String, Object> loginOrRegister(String code, String nickname, String avatarUrl) {
        String openid = getOpenidByCode(code);
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname);
            user.setAvatarUrl(avatarUrl);
            user.setGender(0);
            userMapper.insert(user);
        }
        if ("banned".equals(user.getStatus())) {
            Map<String, Object> result = new HashMap<>();
            result.put("banned", true);
            result.put("message", "您已被封号，请联系管理员处理");
            return result;
        }
        String token = jwtUtil.generateToken(user.getId(), user.getOpenid());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        
        // 每日登录奖励
        int rewardAmount = dailyRewardService.grantReward(user.getId(), "daily_login", 2);
        if (rewardAmount > 0) {
            result.put("rewardMessage", "登录成功，获得" + rewardAmount + "个心动币");
            result.put("rewardAmount", rewardAmount);
        }
        
        return result;
    }

    public Map<String, Object> loginByUsername(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
        if (!password.equals(user.getPassword())) {
            return null;
        }
        if ("banned".equals(user.getStatus())) {
            Map<String, Object> result = new HashMap<>();
            result.put("banned", true);
            result.put("message", "您已被封号，请联系管理员处理");
            return result;
        }
        String token = jwtUtil.generateToken(user.getId(), user.getOpenid());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        
        // 每日登录奖励
        int rewardAmount = dailyRewardService.grantReward(user.getId(), "daily_login", 2);
        if (rewardAmount > 0) {
            result.put("rewardMessage", "登录成功，获得" + rewardAmount + "个心动币");
            result.put("rewardAmount", rewardAmount);
        }
        
        return result;
    }

    public Map<String, Object> registerWithAccount(String username, String password, String nickname, String avatarUrl, String phone, Integer gender) {
        User existUser = userMapper.selectByUsername(username);
        if (existUser != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("errorType", "username");
            return result;
        }

        User existNickname = userMapper.selectByNickname(nickname);
        if (existNickname != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("errorType", "nickname");
            return result;
        }

        String openid = "account_" + username + "_" + System.currentTimeMillis();
        User user = new User();
        user.setOpenid(openid);
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setAvatarUrl(avatarUrl);
        user.setPhone(phone);
        user.setGender(gender != null ? gender : 0);
        userMapper.insert(user);
        String token = jwtUtil.generateToken(user.getId(), user.getOpenid());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    public User getUserInfo(Long userId) {
        return userMapper.selectById(userId);
    }

    public void updateUserInfo(User user) {
        userMapper.updateById(user);
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null || !oldPassword.equals(user.getPassword())) {
            return false;
        }
        User update = new User();
        update.setId(userId);
        update.setPassword(newPassword);
        userMapper.updateById(update);
        return true;
    }

    private String getOpenidByCode(String code) {
        return "mock_openid_" + code;
    }
}
