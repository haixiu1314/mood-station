package com.moodlater.service;

import com.moodlater.entity.Banner;
import com.moodlater.mapper.BannerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class AdminBannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Value("${server.address:localhost}")
    private String serverAddress;

    @Value("${server.port:8080}")
    private String serverPort;

    public List<Banner> getAllBanners() {
        return bannerMapper.selectAll();
    }

    public void addBanner(Banner banner) {
        bannerMapper.insert(banner);
    }

    public void updateBanner(Banner banner) {
        bannerMapper.updateById(banner);
    }

    public void updateStatus(Long id, Integer status) {
        Banner banner = new Banner();
        banner.setId(id);
        banner.setIsActive(status);
        bannerMapper.updateById(banner);
    }

    public void deleteBanner(Long id) {
        bannerMapper.deleteById(id);
    }

    public String uploadImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID() + extension;

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            File dest = new File(dir, newFilename);
            file.transferTo(dest);
            return "http://" + serverAddress + ":" + serverPort + "/uploads/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("上传失败：" + e.getMessage());
        }
    }
}
