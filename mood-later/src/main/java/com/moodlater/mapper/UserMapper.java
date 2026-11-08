package com.moodlater.mapper;

import com.moodlater.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    int insert(User user);

    User selectByOpenid(@Param("openid") String openid);

    User selectByUsername(@Param("username") String username);

    User selectByNickname(@Param("nickname") String nickname);

    User selectById(@Param("id") Long id);

    int updateById(User user);

    int addBalance(@Param("id") Long id, @Param("amount") int amount);
}
