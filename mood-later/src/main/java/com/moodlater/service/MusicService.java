package com.moodlater.service;

import com.moodlater.entity.Music;
import com.moodlater.mapper.MusicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private MusicMapper musicMapper;

    public List<Music> getAllMusic() {
        return musicMapper.selectAll();
    }

    public Music getMusicById(Long id) {
        return musicMapper.selectById(id);
    }

    public int addMusic(Music music) {
        return musicMapper.insert(music);
    }

    public int updateMusic(Music music) {
        return musicMapper.updateById(music);
    }

    public int deleteMusic(Long id) {
        return musicMapper.deleteById(id);
    }

    public Music getRandomMusicByType(String type) {
        return musicMapper.selectRandomByType(type);
    }

    public Music getRandomMusicByTypeAndSinger(String type, String singer) {
        return musicMapper.selectRandomByTypeAndSinger(type, singer);
    }

    public List<Music> getMusicByType(String type) {
        return musicMapper.selectByType(type);
    }

    public List<String> getDistinctSingers(String type) {
        return musicMapper.selectDistinctSingers(type);
    }
}