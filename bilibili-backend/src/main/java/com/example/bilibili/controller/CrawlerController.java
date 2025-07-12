package com.example.bilibili.controller;

import com.example.bilibili.service.CrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    private final CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @PostMapping
    public ResponseEntity<?> crawlVideo(@RequestBody Map<String, String> requestBody) {
        try {
            String videoId = requestBody.get("videoId");
            if (videoId == null || videoId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("请求体中缺少 videoId 参数");
            }
            String result = crawlerService.crawlVideo(videoId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("爬取失败: " + e.getMessage());
        }
    }
}