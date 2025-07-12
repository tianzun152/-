module.exports = {
    // 基本配置
    publicPath: process.env.NODE_ENV === 'production' ? '/your-project-name/' : '/',

    // 开发服务器配置
    devServer: {
        port: 8081,  // 前端开发端口
        host: 'localhost', // <-- 添加这一行，将 host 设置为 localhost
        proxy: {
            '/api': {
                target: 'http://localhost:8080',  // 后端SpringBoot地址
                changeOrigin: true
            }
        }
    },

    // 其他配置...
    lintOnSave: false  // 关闭eslint检查（可选）
}