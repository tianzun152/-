package com.example.bilibili.service;



import com.example.bilibili.entity.BilibiliVideo;

import com.example.bilibili.repository.VideoRepository;

import com.example.bilibili.util.PythonExecutor;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;



import java.util.Date;



@Service

public class CrawlerService {



    @Value("${bilibili.crawler.script-path}")

    private String scriptPath;



    private final PythonExecutor pythonExecutor;

    private final VideoRepository videoRepository;



    public CrawlerService(PythonExecutor pythonExecutor, VideoRepository videoRepository) {

        this.pythonExecutor = pythonExecutor;

        this.videoRepository = videoRepository;

    }



    public String crawlVideo(String videoId) {

        try {

            String jsonResult = pythonExecutor.executePythonScript(scriptPath, videoId);

            if (jsonResult != null) {

                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode videoDataJson = objectMapper.readTree(jsonResult);



// 将 JSON 数据映射到 BilibiliVideo 实体类

                BilibiliVideo video = new BilibiliVideo();

                video.setUrl(videoDataJson.get("url").asText());

                video.setTitle(videoDataJson.get("title").asText());

                video.setUpName(videoDataJson.get("up_name").asText());

                video.setUpId(videoDataJson.get("up_mid").asText());

                video.setViews(videoDataJson.get("views").asInt());

                video.setDanmaku(videoDataJson.get("danmaku").asInt());

                video.setLikes(videoDataJson.get("likes").asInt());

                video.setCoins(videoDataJson.get("coins").asInt());

                video.setFavorites(videoDataJson.get("favorites").asInt());

                video.setShares(videoDataJson.get("shares").asInt());

                video.setPublishDate(videoDataJson.get("publish_date").asText());

                video.setDuration(videoDataJson.get("duration").asInt());

                video.setVideoDesc(videoDataJson.get("description").asText());

                video.setAuthorDesc(videoDataJson.get("author_description").asText());

                video.setTags(videoDataJson.get("tags").asText());

                video.setVideoAid(videoDataJson.get("aid").asText());

                video.setCrawlTime(new Date());



// 保存到数据库

                videoRepository.save(video);

                return "视频信息爬取并保存成功";

            } else {

                return "Python 爬虫执行失败";

            }

        } catch (Exception e) {

            throw new RuntimeException("爬取或保存失败: " + e.getMessage());

        }

    }

}