package com.moodlater.service;

import com.moodlater.entity.Banner;
import com.moodlater.entity.HealingQuote;
import com.moodlater.mapper.BannerMapper;
import com.moodlater.mapper.HealingQuoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private HealingQuoteMapper healingQuoteMapper;

    public List<Banner> getBanners() {
        return bannerMapper.selectActive();
    }

    public HealingQuote getTodayQuote() {
        return healingQuoteMapper.selectRandomActive("daily");
    }
}
