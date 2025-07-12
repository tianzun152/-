package com.example.bilibili.repository;

import com.example.bilibili.entity.BilibiliVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<BilibiliVideo, Long> {
    // 添加缺失的查询方法
    BilibiliVideo findByUrl(String url);

    List<BilibiliVideo> findByUpNameContaining(String upName);

    @Query("SELECT v FROM BilibiliVideo v WHERE v.views > :minViews")
    List<BilibiliVideo> findPopularVideos(int minViews);

    boolean existsByUrl(String url);
}