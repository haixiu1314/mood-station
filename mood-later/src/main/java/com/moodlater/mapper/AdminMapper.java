package com.moodlater.mapper;

import com.moodlater.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminMapper {

    List<User> selectAllUsers();

    User selectUserById(@Param("id") Long id);

    int updateUserStatus(@Param("id") Long id, @Param("status") String status);

    int deleteUserById(@Param("id") Long id);

    int updateCoinBalance(@Param("id") Long id, @Param("amount") Integer amount);

    User selectAdminByUsername(@Param("username") String username);
}
