package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Music;
import com.moodlater.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping("/list")
    public Result<List<Music>> getAllMusic() {
        List<Music> musicList = musicService.getAllMusic();
        return Result.success(musicList);
    }

    @GetMapping("/{id}")
    public Result<Music> getMusic(@PathVariable Long id) {
        Music music = musicService.getMusicById(id);
        return Result.success(music);
    }

    @GetMapping("/random")
    public Result<Music> getRandomMusic(@RequestParam String type, @RequestParam(required = false) String singer) {
        Music music;
        if (singer != null && !singer.isEmpty()) {
            music = musicService.getRandomMusicByTypeAndSinger(type, singer);
        } else {
            music = musicService.getRandomMusicByType(type);
        }
        if (music == null) {
            return Result.error("暂无可用音乐");
        }
        return Result.success(music);
    }

    @GetMapping("/singers")
    public Result<List<String>> getSingers(@RequestParam String type) {
        List<String> singers = musicService.getDistinctSingers(type);
        return Result.success(singers);
    }
}