package com.example.bilibili.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bilibili_videos")
public class BilibiliVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;

    @Column(name = "up_name")
    private String upName;

    @Column(name = "up_id")
    private String upId;
    private Integer views;
    private Integer danmaku;
    private Integer likes;
    private Integer coins;
    private Integer favorites;
    private Integer shares;

    @Column(name = "publish_date")
    private String publishDate;
    private Integer duration;

    @Column(name = "video_desc", length = 2000)
    private String videoDesc;

    @Column(name = "author_desc", length = 2000)
    private String authorDesc;

    @Column(length = 1000)
    private String tags;

    @Column(name = "video_aid")
    private String videoAid;

    @Column(name = "crawl_time")
    private Date crawlTime;

    // 必须添加所有字段的getter和setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getUpName() { return upName; }
    public void setUpName(String upName) { this.upName = upName; }
    public String getUpId() { return upId; }
    public void setUpId(String upId) { this.upId = upId; }
    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }
    public Integer getDanmaku() { return danmaku; }
    public void setDanmaku(Integer danmaku) { this.danmaku = danmaku; }
    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }
    public Integer getCoins() { return coins; }
    public void setCoins(Integer coins) { this.coins = coins; }
    public Integer getFavorites() { return favorites; }
    public void setFavorites(Integer favorites) { this.favorites = favorites; }
    public Integer getShares() { return shares; }
    public void setShares(Integer shares) { this.shares = shares; }
    public String getPublishDate() { return publishDate; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getVideoDesc() { return videoDesc; }
    public void setVideoDesc(String videoDesc) { this.videoDesc = videoDesc; }
    public String getAuthorDesc() { return authorDesc; }
    public void setAuthorDesc(String authorDesc) { this.authorDesc = authorDesc; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getVideoAid() { return videoAid; }
    public void setVideoAid(String videoAid) { this.videoAid = videoAid; }
    public Date getCrawlTime() { return crawlTime; }
    public void setCrawlTime(Date crawlTime) { this.crawlTime = crawlTime; }
}