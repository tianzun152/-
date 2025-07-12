import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 请求拦截器
api.interceptors.request.use(config => {
    console.log('发起请求:', config.url, config.data);
    return config;
}, error => {
    return Promise.reject(error);
});

// 响应拦截器
api.interceptors.response.use(response => {
    return response.data; // 关键修改：返回 response.data
}, error => {
    if (error.code === 'ERR_NETWORK') {
        console.error('网络连接错误...');
    }
    return Promise.reject(error);
});

export default {
    getVideos() {
        return api.get('/videos');
    },
    getVideoById(id) {
        return api.get(`/videos/${id}`);
    },
    createVideo(videoData) {
        return api.post('/videos', videoData);
    },
    updateVideo(id, videoData) {
        return api.put(`/videos/${id}`, videoData);
    },
    deleteVideo(id) {
        return api.delete(`/videos/${id}`);
    },
    crawlVideo(videoIdOrUrl) {
        return api.post('/crawler', { videoId: videoIdOrUrl });
    }
};