// 跨域配置
module.exports = {
    assetsDir:'static',
    parallel:false,
    publicPath: './',
    devServer: {
        port: 8000,
        proxy: {
            '/api': {
                target: 'http://118.195.192.23:8081/',
                // target: 'http://localhost:8081/',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }
}
