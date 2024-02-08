package xyz.pplax.spider.utils;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class AsyncHttpUtil {

    @Autowired
    private AsyncHttpClient asyncHttpClient;

    private final static Logger logger = LoggerFactory.getLogger(AsyncHttpUtil.class);

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
}
