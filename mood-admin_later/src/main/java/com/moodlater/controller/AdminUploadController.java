package com.moodlater.controller;

import com.moodlater.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/upload")
public class AdminUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file,
                                                    @RequestParam(value = "type", defaultValue = "common") String type) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.error("文件名不能为空");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String[] allowedTypes = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
        boolean allowed = false;
        for (String allowedType : allowedTypes) {
            if (allowedType.equalsIgnoreCase(suffix)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            return Result.error("仅支持 JPG/PNG/GIF/WEBP 格式图片");
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.error("文件大小不能超过2MB");
        }
        String dirName;
        switch (type) {
            case "banner":
                dirName = "banner";
                break;
            case "product":
                dirName = "product";
                break;
            default:
                dirName = "common";
                break;
        }
        String fileName = UUID.randomUUID().toString() + suffix;
        File dir = new File(uploadPath + File.separator + dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, fileName);
        try {
            file.transferTo(dest);
            Map<String, String> data = new HashMap<>();
            data.put("url", "/uploads/" + dirName + "/" + fileName);
            data.put("fileName", fileName);
            return Result.success(data);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}
