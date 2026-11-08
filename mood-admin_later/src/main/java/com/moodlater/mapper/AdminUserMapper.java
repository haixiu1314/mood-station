package com.moodlater.mapper;

import com.moodlater.entity.User;
import com.moodlater.entity.CoinLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminUserMapper {
    List<User> selectAllUsers();
    User selectUserById(@Param("id") Long id);
    int updateUserStatus(@Param("id") Long id, @Param("status") String status);
    int deleteUser(@Param("id") Long id);
    int deleteUserRelatedData(@Param("id") Long id);
    int deleteUserRelatedData2(@Param("id") Long id);
    int deleteUserRelatedData3(@Param("id") Long id);
    int deleteUserRelatedData4(@Param("id") Long id);
    int deleteUserRelatedData5(@Param("id") Long id);
    int deleteUserRelatedData6(@Param("id") Long id);
    int deleteUserRelatedData7(@Param("id") Long id);
    int deleteUserRelatedData8(@Param("id") Long id);
    int deleteUserRelatedData9(@Param("id") Long id);
    int deleteUserRelatedData10(@Param("id") Long id);
    int deleteUserRelatedData11(@Param("id") Long id);
    int deleteUserRelatedData12(@Param("id") Long id);
    int deleteUserRelatedData13(@Param("id") Long id);
    int updateCoinBalance(@Param("id") Long id, @Param("amount") Integer amount);

    int insertCoinLog(CoinLog coinLog);
}
