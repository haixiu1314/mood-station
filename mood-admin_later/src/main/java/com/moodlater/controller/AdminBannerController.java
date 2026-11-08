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
    private AdminBannerService bannerService;

    @GetMapping("/list")
    public Result<List<Banner>> list() {
        return Result.success(bannerService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Banner> getById(@PathVariable Long id) {
        return Result.success(bannerService.getById(id));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Banner banner) {
        bannerService.add(banner);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Banner banner) {
        bannerService.update(banner);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        bannerService.updateStatus(id, params.get("isActive"));
        return Result.success();
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        String imageUrl = bannerService.uploadImage(file);
        return Result.success(imageUrl);
    }
}
