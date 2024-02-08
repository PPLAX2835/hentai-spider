package xyz.pplax.spiderdemo.utils;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class AsyncHttpUtil {

    private final AsyncHttpClient asyncHttpClient;

    @Autowired
    public AsyncHttpUtil(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    public String sendGetRequest(String url) {
        try {
            Response response = asyncHttpClient.prepareGet(url).execute().get();
            return response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            System.out.println("Now retrying");
            return sendGetRequest(url);
        }
    }

    public String sendGetRequest(String url, Map<String, String> headers){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response.getResponseBody();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println("Now retrying");
            return sendGetRequest(url, headers);
        }
    }

    public String sendPostRequest(String url, Map<String, String> params){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormParam(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response.getResponseBody();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println("Now retrying");
            return sendGetRequest(url, params);
        }
    }

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
            System.out.println(e.getMessage());
            System.out.println("Now retrying");
            return sendGetRequest(url, params);
        }
    }
}
