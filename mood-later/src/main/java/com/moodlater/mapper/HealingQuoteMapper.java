package com.moodlater.mapper;

import com.moodlater.entity.HealingQuote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HealingQuoteMapper {

    List<HealingQuote> selectActiveByCategory(@Param("category") String category);

    HealingQuote selectRandomActive(@Param("category") String category);

    int insert(HealingQuote quote);

    int updateById(HealingQuote quote);
}
