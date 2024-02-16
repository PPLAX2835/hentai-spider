package xyz.pplax.spider.config;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.proxy.ProxyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncHttpClientConfigurator {

    @Value("${pplax.spider.proxy.host:127.0.0.1}")
    private String host;

    @Value("${pplax.spider.proxy.port:7890}")
    private Integer port;

    @Value("${pplax.spider.proxy.enable:false}")
    private Boolean enableProxy;

    @Bean
    public AsyncHttpClient createAsyncHttpClient() {
        DefaultAsyncHttpClientConfig.Builder configBuilder = new DefaultAsyncHttpClientConfig.Builder()
                .setMaxConnections(30)                  // 最大连接数，这个要大于等于最大线程数，要不然有些连接会被拒绝
                .setMaxConnectionsPerHost(20)
                .setConnectTimeout(10000)
                .setRequestTimeout(10000)
                .setReadTimeout(10000)
                .setFollowRedirect(true);

        if (enableProxy) {
            configBuilder.setProxyServer(new ProxyServer.Builder(host, port).build());
        }

        AsyncHttpClientConfig config = configBuilder.build();

        return new DefaultAsyncHttpClient(config);
    }
}
