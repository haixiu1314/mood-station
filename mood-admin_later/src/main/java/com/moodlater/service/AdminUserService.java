package com.moodlater.service;

import com.moodlater.entity.CoinLog;
import com.moodlater.entity.User;
import com.moodlater.mapper.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminUserService {

    @Autowired
    private AdminUserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    public List<User> listAllUsers() {
        return userMapper.selectAllUsers();
    }

    public User getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    @Transactional
    public void banUser(Long id) {
        userMapper.updateUserStatus(id, "banned");
    }

    @Transactional
    public void unbanUser(Long id) {
        userMapper.updateUserStatus(id, "normal");
    }

    @Transactional
    public void deleteUser(Long id) {
        // 按外键依赖顺序逐个删除关联数据
        userMapper.deleteUserRelatedData(id);
        userMapper.deleteUserRelatedData2(id);
        userMapper.deleteUserRelatedData3(id);
        userMapper.deleteUserRelatedData4(id);
        userMapper.deleteUserRelatedData5(id);
        userMapper.deleteUserRelatedData6(id);
        userMapper.deleteUserRelatedData7(id);
        userMapper.deleteUserRelatedData8(id);
        userMapper.deleteUserRelatedData9(id);
        userMapper.deleteUserRelatedData10(id);
        userMapper.deleteUserRelatedData11(id);
        userMapper.deleteUserRelatedData12(id);
        userMapper.deleteUserRelatedData13(id);
        // 最后删除用户
        userMapper.deleteUser(id);
    }

    @Transactional
    public void giftCoins(Long userId, Integer amount, Long operatorId, String reason) {
        userMapper.updateCoinBalance(userId, amount);
        User user = userMapper.selectUserById(userId);
        CoinLog coinLog = new CoinLog();
        coinLog.setUserId(userId);
        coinLog.setChangeAmount(amount);
        coinLog.setBalanceAfter(user.getCoinBalance());
        coinLog.setType("gift");
        coinLog.setReason(reason);
        coinLog.setOperatorId(operatorId);
        userMapper.insertCoinLog(coinLog);
        
        // 发送通知
        notificationService.sendNotification(userId, "system", "coin_gift", "心动币到账",
                "你收到了" + amount + "心动币，管理员希望你每天保持开心哦(づ￣3￣)づ╭❤～", null);
    }
}
