package xyz.pplax.spider.utils;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.spiders.FurAffinitySpider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class AsyncHttpUtil {

    @Autowired
    private AsyncHttpClient asyncHttpClient;

    @Autowired
    private Executor threadPoolTaskExecutor;

    private final static Logger logger = LoggerFactory.getLogger(AsyncHttpUtil.class);

    /**
     * 对url列表进行批量请求
     * @param urlList
     * @return
     */
    public CompletableFuture<List<String>> sendGetRequestBatch(List<String> urlList) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (String url : urlList) {

            logger.info(String.format("正在获取：%s的页面内容", url));

            // 提交任务
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return sendGetRequest(url);
            }, threadPoolTaskExecutor);
            futures.add(future);

        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        return allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );
    }

    /**
     * 发送get请求
     * @param url
     * @return
     */
    public String sendGetRequest(String url) {
        try {
            Response response = asyncHttpClient.prepareGet(url).execute().get();
            return response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return sendGetRequest(url);
        }
    }

    /**
     * 进行get请求，返回Response
     * @param url
     * @return
     */
    public Response get(String url) {
        try {
            return asyncHttpClient.prepareGet(url).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return get(url);
        }
    }

    /**
     * 发送get请求，携带headers
     * @param url
     * @param headers
     * @return
     */
    public String sendGetRequest(String url, Map<String, String> headers){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response.getResponseBody();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return sendGetRequest(url, headers);
        }
    }

    /**
     * 发送get请求，携带headers，返回response
     * @param url
     * @param headers
     * @return
     */
    public Response get(String url, Map<String, String> headers){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            return requestBuilder.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return get(url, headers);
        }
    }

    /**
     * 发送post请求，带参数
     * @param url
     * @param params
     * @return
     */
    public String sendPostRequest(String url, Map<String, String> params){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormParam(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response.getResponseBody();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return sendGetRequest(url, params);
        }
    }

    /**
     * 发送post请求，带参数
     * @param url
     * @param params
     * @return
     */
    public Response post(String url, Map<String, String> params){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormParam(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response;
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return post(url, params);
        }
    }


    /**
     * 发送post请求，带参数和headers
     * @param url
     * @param params
     * @return
     */
    public String sendPostRequest(String url, Map<String, String> params, Map<String, String> headers) {
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormParam(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response.getResponseBody();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return sendGetRequest(url, params);
        }
    }

    /**
     * 发送post请求，带参数和headers，返回response
     * @param url
     * @param params
     * @return
     */

    public Response post(String url, Map<String, String> params, Map<String, String> headers) {
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormParam(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response;
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            logger.warn("Now retrying");
            return post(url, params);
        }
    }

}
