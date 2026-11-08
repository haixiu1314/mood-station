package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Banner;
import com.moodlater.service.AdminBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/banner")
public class AdminBannerController {

    @Autowired
    private AdminBannerService adminBannerService;

    @GetMapping("/list")
    public Result<List<Banner>> getBannerList() {
        List<Banner> banners = adminBannerService.getAllBanners();
        return Result.success(banners);
    }

    @PostMapping("/add")
    public Result<Void> addBanner(@RequestBody Banner banner) {
        banner.setIsActive(1);
        adminBannerService.addBanner(banner);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateBanner(@RequestBody Banner banner) {
        adminBannerService.updateBanner(banner);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("isActive");
        adminBannerService.updateStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteBanner(@PathVariable Long id) {
        adminBannerService.deleteBanner(id);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result<String> uploadBannerImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        String imageUrl = adminBannerService.uploadImage(file);
        return Result.success(imageUrl);
    }
}
