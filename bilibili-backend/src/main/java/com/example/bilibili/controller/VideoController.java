package com.example.bilibili.controller;

import com.example.bilibili.entity.BilibiliVideo;
import com.example.bilibili.service.VideoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public List<BilibiliVideo> getAllVideos() {
        return videoService.getAll();
    }

    @GetMapping("/{id}")
    public BilibiliVideo getVideoById(@PathVariable Long id) {
        return videoService.getById(id).orElse(null);
    }

    @PostMapping
    public BilibiliVideo createVideo(@RequestBody BilibiliVideo video) {
        return videoService.save(video);
    }

    @PutMapping("/{id}")
    public BilibiliVideo updateVideo(@PathVariable Long id, @RequestBody BilibiliVideo video) {
        video.setId(id);
        return videoService.save(video);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoService.delete(id);
    }
}