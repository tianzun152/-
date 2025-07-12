import re
import requests
import time
from bs4 import BeautifulSoup
import datetime
import json
import argparse
import sys

# 请求头设置
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
}

def safe_request(url, max_retries=3):
    """带重试机制的安全请求函数"""
    for attempt in range(max_retries):
        try:
            response = requests.get(url, headers=HEADERS, timeout=10)
            response.raise_for_status()
            return response
        except requests.exceptions.RequestException as e:
            print(f"请求 {url} 失败 (尝试 {attempt + 1}/{max_retries}): {e}", file=sys.stderr)
            if attempt == max_retries - 1:
                raise
            time.sleep(2 ** attempt)  # 指数退避
    return None

def is_url(video_id_or_url):
    """判断输入是否为URL"""
    return video_id_or_url.startswith(("http://", "https://"))

def get_video_url(video_id_or_url):
    """获取完整的视频URL"""
    if is_url(video_id_or_url):
        return video_id_or_url
    return f"https://www.bilibili.com/video/{video_id_or_url}"

def extract_initial_state_data(initial_state_text):
    """从初始状态脚本中提取数据"""
    patterns = {
        "author_id": r'"mid":(\d+)',
        "video_aid": r'"aid":(\d+)',
        "video_duration": r'"duration":(\d+)'
    }

    result = {}
    for key, pattern in patterns.items():
        match = re.search(pattern, initial_state_text)
        result[key] = match.group(1) if match else None

    # 视频时长调整
    if result.get("video_duration"):
        result["video_duration"] = int(result["video_duration"]) - 2

    return result

def extract_meta_data(meta_description):
    """从meta描述中提取各种数据"""
    numbers_match = re.search(
        r'[\s\S]*?视频播放量 (\d+)、弹幕量 (\d+)、点赞数 (\d+)、投硬币枚数 (\d+)、收藏人数 (\d+)、转发人数 (\d+)',
        meta_description)
    numbers = numbers_match.groups() if numbers_match else None

    author_search = re.search(r"视频作者\s*([^,]+)", meta_description)
    author = author_search.group(1).strip() if author_search else "未找到作者"

    author_desc_match = re.search(r'作者简介 (.+?),', meta_description)
    author_desc = author_desc_match.group(1) if author_desc_match else "未找到作者简介"

    meta_parts = re.split(r',\s*', meta_description)
    video_desc = meta_parts[0].strip() if meta_parts else "未找到视频简介"

    return {
        "numbers": numbers,
        "author": author,
        "author_desc": author_desc,
        "video_desc": video_desc
    }

def crawl_bilibili_video(video_id_or_url):
    """爬取单个 Bilibili 视频信息，返回字典"""
    url = get_video_url(video_id_or_url)
    try:
        response = safe_request(url)
        if not response:
            return None
        soup = BeautifulSoup(response.text, "html.parser")

        initial_state_script = soup.find("script", string=re.compile("window.__INITIAL_STATE__"))
        if not initial_state_script:
            print(f"警告: 未找到 INITIAL_STATE 脚本: {url}", file=sys.stderr)
            return None
        initial_state_data = extract_initial_state_data(initial_state_script.string)

        title_raw = soup.find("title").text
        title = re.sub(r"_哔哩哔哩_bilibili", "", title_raw).strip()

        keywords_meta = soup.find("meta", itemprop="keywords")
        tags_str = keywords_meta["content"].replace(title + ',', '') if keywords_meta and 'content' in keywords_meta.attrs else ""
        tags = ",".join(tags_str.split(',')[:-4]) if tags_str.count(',') > 3 else tags_str

        meta_description_tag = soup.find("meta", itemprop="description")
        if not meta_description_tag or 'content' not in meta_description_tag.attrs:
            print(f"警告: 未找到描述信息: {url}", file=sys.stderr)
            return None
        meta_data = extract_meta_data(meta_description_tag["content"])

        if not meta_data["numbers"]:
            print(f"警告: {url} 未找到关键数据，可能为分集视频", file=sys.stderr)
            return None

        views, danmaku, likes, coins, favorites, shares = [int(n) for n in meta_data["numbers"]]
        publish_date = soup.find("meta", itemprop="uploadDate")["content"] if soup.find("meta", itemprop="uploadDate") and 'content' in soup.find("meta", itemprop="uploadDate").attrs else None

        return {
            "title": title,
            "url": url,
            "up_name": meta_data["author"],
            "up_mid": initial_state_data.get("author_id"),
            "views": views,
            "danmaku": danmaku,
            "likes": likes,
            "coins": coins,
            "favorites": favorites,
            "shares": shares,
            "publish_date": publish_date,
            "duration": initial_state_data.get("video_duration"),
            "description": meta_data["video_desc"],
            "author_description": meta_data["author_desc"],
            "tags": tags,
            "aid": initial_state_data.get("video_aid")
        }

    except Exception as e:
        print(f"爬取 {url} 发生错误: {e}", file=sys.stderr)
        return None

def main():
    parser = argparse.ArgumentParser(description="爬取单个 Bilibili 视频信息并以 JSON 格式输出。")
    parser.add_argument("video_id_or_url", help="Bilibili 视频 ID 或 URL。")
    args = parser.parse_args()

    video_data = crawl_bilibili_video(args.video_id_or_url)

    if video_data:
        print(json.dumps(video_data, ensure_ascii=False))
    else:
        print("爬取失败，请查看错误信息。", file=sys.stderr)

if __name__ == "__main__":
    main()