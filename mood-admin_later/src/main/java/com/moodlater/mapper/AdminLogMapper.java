package com.moodlater.mapper;

import com.moodlater.entity.AdminLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminLogMapper {
    int insertLog(AdminLog log);
    List<AdminLog> selectLogs(@Param("adminId") Long adminId);
}
