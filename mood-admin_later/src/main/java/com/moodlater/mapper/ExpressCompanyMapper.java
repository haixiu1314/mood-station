package com.moodlater.mapper;

import com.moodlater.entity.ExpressCompany;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ExpressCompanyMapper {
    List<ExpressCompany> selectAll();
}
