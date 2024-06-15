package xyz.pplax.spider.config;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.proxy.ProxyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.pplax.spider.model.pojo.Config;

@Configuration
public class AsyncHttpClientConfigurator {

    @Autowired
    private Config systemConfig;

    @Bean
    public AsyncHttpClient createAsyncHttpClient() {
        DefaultAsyncHttpClientConfig.Builder configBuilder = new DefaultAsyncHttpClientConfig.Builder()
                .setMaxConnections(30)                  // 最大连接数，这个要大于等于最大线程数，要不然有些连接会被拒绝
                .setMaxConnectionsPerHost(20)
                .setConnectTimeout(600000)
                .setRequestTimeout(10000)
                .setReadTimeout(10000)
                .setFollowRedirect(true);

        if (systemConfig.getProxyEnable()) {
            configBuilder.setProxyServer(new ProxyServer.Builder(systemConfig.getProxyHost(), systemConfig.getProxyPort()).build());
        }

        AsyncHttpClientConfig config = configBuilder.build();

        return new DefaultAsyncHttpClient(config);
    }
}
