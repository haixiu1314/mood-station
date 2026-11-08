package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Banner;
import com.moodlater.entity.HealingQuote;
import com.moodlater.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/banners")
    public Result<List<Banner>> getBanners() {
        List<Banner> banners = homeService.getBanners();
        return Result.success(banners);
    }

    @GetMapping("/quote")
    public Result<HealingQuote> getTodayQuote() {
        HealingQuote quote = homeService.getTodayQuote();
        return Result.success(quote);
    }
}
