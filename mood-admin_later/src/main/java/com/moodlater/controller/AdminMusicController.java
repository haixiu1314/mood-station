package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Music;
import com.moodlater.service.AdminMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/music")
public class AdminMusicController {

    @Autowired
    private AdminMusicService musicService;

    @GetMapping("/list")
    public Result<List<Music>> list() {
        return Result.success(musicService.listAllMusic());
    }

    @GetMapping("/{id}")
    public Result<Music> getById(@PathVariable Long id) {
        return Result.success(musicService.getMusicById(id));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Music music) {
        musicService.addMusic(music);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Music music) {
        musicService.updateMusic(music);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        musicService.deleteMusic(id);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        String fileUrl = musicService.uploadMusicFile(file);
        return Result.success(fileUrl);
    }
}