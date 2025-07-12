package com.example.bilibili.service;

import com.example.bilibili.entity.BilibiliVideo;
import com.example.bilibili.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<BilibiliVideo> getAll() {
        return videoRepository.findAll();
    }

    public Optional<BilibiliVideo> getById(Long id) {
        return videoRepository.findById(id);
    }

    @Transactional
    public BilibiliVideo save(BilibiliVideo video) {
        // 检查URL是否已存在
        if (video.getId() == null && video.getUrl() != null &&
                videoRepository.existsByUrl(video.getUrl())) {
            BilibiliVideo existing = videoRepository.findByUrl(video.getUrl());
            video.setId(existing.getId());
        }
        return videoRepository.save(video);
    }

    @Transactional
    public List<BilibiliVideo> saveAll(List<BilibiliVideo> videos) {
        return videoRepository.saveAll(videos);
    }

    @Transactional
    public void delete(Long id) {
        videoRepository.deleteById(id);
    }

    public List<BilibiliVideo> searchByUpName(String upName) {
        return videoRepository.findByUpNameContaining(upName);
    }

    public List<BilibiliVideo> getPopularVideos(int minViews) {
        return videoRepository.findPopularVideos(minViews);
    }
}