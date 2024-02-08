package xyz.pplax.spider.config;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncHttpClientConfigurator {

    @Bean
    public static AsyncHttpClient createAsyncHttpClient() {
        AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder()
                .setMaxConnections(100) // 设置最大连接数
                .setMaxConnectionsPerHost(20) // 每个主机的最大连接数
                .setConnectTimeout(5000) // 连接超时时间（毫秒）
                .setRequestTimeout(5000) // 请求超时时间（毫秒）
                .setReadTimeout(5000) // 读取超时时间（毫秒）
                .setFollowRedirect(true) // 是否自动处理重定向
                .build();

        return new DefaultAsyncHttpClient(config);
    }
}

