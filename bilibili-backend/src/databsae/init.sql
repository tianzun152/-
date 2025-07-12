-- 初始化数据库
CREATE DATABASE IF NOT EXISTS `bilibili_data` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `bilibili_data`;

-- 视频表
CREATE TABLE IF NOT EXISTS `bilibili_videos` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                 `title` VARCHAR(255) NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    `up_name` VARCHAR(100),
    `up_id` VARCHAR(50),
    `views` INT DEFAULT 0,
    `likes` INT DEFAULT 0,
    `publish_time` DATETIME,
    `crawl_time` DATETIME,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_url` (`url`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;