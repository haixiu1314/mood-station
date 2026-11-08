package com.moodlater.service;

import com.moodlater.entity.Banner;
import com.moodlater.mapper.AdminBannerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AdminBannerService {

    @Autowired
    private AdminBannerMapper bannerMapper;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    public List<Banner> listAll() {
        return bannerMapper.selectAll();
    }

    public Banner getById(Long id) {
        return bannerMapper.selectById(id);
    }

    public void add(Banner banner) {
        if (banner.getIsActive() == null) {
            banner.setIsActive(1);
        }
        if (banner.getSortOrder() == null) {
            banner.setSortOrder(0);
        }
        bannerMapper.insert(banner);
    }

    public void update(Banner banner) {
        bannerMapper.update(banner);
    }

    public void delete(Long id) {
        bannerMapper.deleteById(id);
    }

    public void updateStatus(Long id, Integer isActive) {
        bannerMapper.updateStatus(id, isActive);
    }

    public String uploadImage(MultipartFile file) {
        try {
            // 确保上传目录存在
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = uploadDir.resolve(fileName);
            file.transferTo(filePath.toFile());

            // 返回访问URL
            return "http://localhost:8081/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }
}
