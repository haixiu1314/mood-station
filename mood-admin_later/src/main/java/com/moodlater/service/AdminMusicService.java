package com.moodlater.service;

import com.moodlater.entity.Music;
import com.moodlater.mapper.AdminMusicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AdminMusicService {

    @Autowired
    private AdminMusicMapper musicMapper;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    public List<Music> listAllMusic() {
        return musicMapper.selectAll();
    }

    public Music getMusicById(Long id) {
        return musicMapper.selectById(id);
    }

    @Transactional
    public void addMusic(Music music) {
        if (music.getIsActive() == null) {
            music.setIsActive(1);
        }
        musicMapper.insert(music);
    }

    @Transactional
    public void updateMusic(Music music) {
        musicMapper.updateById(music);
    }

    @Transactional
    public void deleteMusic(Long id) {
        Music music = musicMapper.selectById(id);
        if (music != null && music.getFileUrl() != null) {
            String filePath = music.getFileUrl().replace("http://localhost:8081/uploads/", uploadPath + "/");
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        musicMapper.deleteById(id);
    }

    public String uploadMusicFile(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(uploadPath, "music");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadDir.resolve(fileName);
            file.transferTo(filePath.toFile());
            // 返回 8080 端口的 URL，因为小程序从 mood-later 请求
            return "http://localhost:8080/uploads/music/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("音乐文件上传失败", e);
        }
    }
}