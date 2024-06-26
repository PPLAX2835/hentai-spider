package xyz.pplax.spider.utils;

import com.alibaba.fastjson2.JSON;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.dao.FileDao;
import xyz.pplax.spider.model.pojo.Config;
import xyz.pplax.spider.model.pojo.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
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

    @Autowired
    private FileDao fileDao;

    @Autowired
    private Config systemConfig;

    private final static Logger logger = LoggerFactory.getLogger(AsyncHttpUtil.class);

    /**
     * 对url列表进行批量请求
     * @param urlList
     * @return
     */
    public List<String> sendGetRequestBatch(List<String> urlList) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (String url : urlList) {

            logger.info(String.format("正在获取：%s的页面内容", url));

            // 提交任务
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return sendGetRequest(url, 0);
            }, threadPoolTaskExecutor);
            futures.add(future);

        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        CompletableFuture<List<String>> listCompletableFuture = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );

        return listCompletableFuture.join();
    }

    /**
     * 对url列表进行批量请求，携带headers
     * @param urlList
     * @return
     */
    public List<String> sendGetRequestBatch(List<String> urlList, Map<String,String> headers) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (String url : urlList) {

            logger.info(String.format("正在获取：%s的页面内容", url));

            // 提交任务
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return sendGetRequest(url, headers, 0);
            }, threadPoolTaskExecutor);
            futures.add(future);

        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        CompletableFuture<List<String>> listCompletableFuture = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );

        return listCompletableFuture.join();
    }

    /**
     * 对url列表进行批量请求
     * @param urlList
     * @return
     */
    public List<Response> getBatch(List<String> urlList) {
        List<CompletableFuture<Response>> futures = new ArrayList<>();

        for (String url : urlList) {

            logger.info(String.format("正在获取：%s的页面内容", url));

            // 提交任务
            CompletableFuture<Response> future = CompletableFuture.supplyAsync(() -> {
                return get(url, 0);
            }, threadPoolTaskExecutor);
            futures.add(future);

        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        CompletableFuture<List<Response>> listCompletableFuture = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );

        return listCompletableFuture.join();
    }

    /**
     * 对url列表进行批量请求，携带headers
     * @param urlList
     * @return
     */
    public List<Response> getBatch(List<String> urlList, Map<String,String> headers) {
        List<CompletableFuture<Response>> futures = new ArrayList<>();

        for (String url : urlList) {

            logger.info(String.format("正在获取：%s的页面内容", url));

            // 提交任务
            CompletableFuture<Response> future = CompletableFuture.supplyAsync(() -> {
                return get(url, headers, 0);
            }, threadPoolTaskExecutor);
            futures.add(future);

        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        CompletableFuture<List<Response>> listCompletableFuture = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );

        return listCompletableFuture.join();
    }

    /**
     * 下载
     * @param file
     * @throws IOException
     */
    public Boolean download(File file) throws IOException {

        // 判断文件是否已经下载过
        if (FileUtils.fileExists(systemConfig.getBasePath() + file.getFilePath() + file.getFileName())) {
            logger.warn("文件已经下载过了");
            logger.info("文件信息：" + JSON.toJSONString(file));
            return false;
        }

        // 获得响应
        Response response = get(file.getFileUrl(), 0);

        // 从响应中获取输入流
        ReadableByteChannel inputChannel = Channels.newChannel(response.getResponseBodyAsStream());

        // 创建目录
        FileUtils.createDirectory(systemConfig.getBasePath() + file.getFilePath());

        // 创建本地文件输出流
        FileOutputStream fos = new FileOutputStream(systemConfig.getBasePath() + file.getFilePath() + file.getFileName());
        FileChannel outputChannel = fos.getChannel();

        // 将输入流的内容写入本地文件
        outputChannel.transferFrom(inputChannel, 0, Long.MAX_VALUE);

        // 持久化到数据库
        File existFile = fileDao.selectByPlatformIdAndIdInPlatform(file.getPlatformId(), file.getIdInPlatform());
        if (existFile == null) {
            fileDao.insert(file);
        }

        // 关闭资源
        inputChannel.close();
        outputChannel.close();
        fos.close();

        return true;
    }

    /**
     * 下载，携带headers
     * @param file
     * @param headers
     * @return
     * @throws IOException
     */
    public Boolean download(File file, Map<String, String> headers) throws IOException {

        File existFile = fileDao.selectByPlatformIdAndIdInPlatform(file.getPlatformId(), file.getIdInPlatform());

        // 判断文件是否已经下载过
        if (FileUtils.fileExists(systemConfig.getBasePath() + file.getFilePath() + file.getFileName())) {
            logger.warn("文件已经下载过了");

            // 持久化到数据库
            if (existFile == null) {
                fileDao.insert(file);
            }

            logger.info("文件信息：" + JSON.toJSONString(file));
            return false;
        }

        // 获得响应
        Response response = get(file.getFileUrl(), headers, 0);

        // 从响应中获取输入流
        ReadableByteChannel inputChannel = Channels.newChannel(response.getResponseBodyAsStream());

        // 创建目录
        FileUtils.createDirectory(systemConfig.getBasePath() + file.getFilePath());

        // 创建本地文件输出流
        FileOutputStream fos = new FileOutputStream(systemConfig.getBasePath() + file.getFilePath() + file.getFileName());
        FileChannel outputChannel = fos.getChannel();

        // 将输入流的内容写入本地文件
        outputChannel.transferFrom(inputChannel, 0, Long.MAX_VALUE);

        // 持久化到数据库
        if (existFile == null) {
            fileDao.insert(file);
        }

        // 关闭资源
        inputChannel.close();
        outputChannel.close();
        fos.close();

        return true;
    }

    /**
     * 批量下载
     * @param fileList
     */
    public void downloadBatch(List<File> fileList) {
        List<CompletableFuture<Boolean>> futureList = new ArrayList<>();

        for (File file : fileList) {
            CompletableFuture<Boolean> booleanCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return download(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }, threadPoolTaskExecutor);

            futureList.add(booleanCompletableFuture);
        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
    }

    /**
     * 批量下载，携带参数
     * @param fileList
     * @param headers
     */
    public void downloadBatch(List<File> fileList, Map<String,String> headers) {
        List<CompletableFuture<Boolean>> futureList = new ArrayList<>();

        for (File file : fileList) {
            CompletableFuture<Boolean> booleanCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return download(file, headers);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }, threadPoolTaskExecutor);

            futureList.add(booleanCompletableFuture);
        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
    }

    /**
     * 发送get请求
     * @param url
     * @param num
     * @return
     */
    public String sendGetRequest(String url, int num) {
        try {
            Response response = asyncHttpClient.prepareGet(url).execute().get();
            return response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return "";
            }

            logger.warn("Now retrying");
            return sendGetRequest(url, num + 1);
        }
    }

    /**
     * 进行get请求，返回Response
     * @param url
     * @param num
     * @return
     */
    public Response get(String url, int num) {
        try {
            return asyncHttpClient.prepareGet(url).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return null;
            }

            logger.warn("Now retrying");
            return get(url, num + 1);
        }
    }

    /**
     * 送get请求，携带headers
     * @param url
     * @param headers
     * @param num
     * @return
     */
    public String sendGetRequest(String url, Map<String, String> headers, int num){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response.getResponseBody();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return "";
            }

            logger.warn("Now retrying");
            return sendGetRequest(url, headers, num + 1);
        }
    }

    /**
     * 发送get请求，携带headers，返回response
     * @param url
     * @param headers
     * @param num
     * @return
     */
    public Response get(String url, Map<String, String> headers, int num){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        try {
            return requestBuilder.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return null;
            }

            logger.warn("Now retrying");
            return get(url, headers, num + 1);
        }
    }

    /**
     * 发送post请求，带参数
     * @param url
     * @param params
     * @param num
     * @return
     */
    public String sendPostRequest(String url, Map<String, String> params, int num){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormParam(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response.getResponseBody();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return null;
            }

            logger.warn("Now retrying");
            return sendPostRequest(url, params, num + 1);
        }
    }

    /**
     * 发送post请求，带参数
     * @param url
     * @param params
     * @param num
     * @return
     */
    public Response post(String url, Map<String, String> params, int num){
        org.asynchttpclient.BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormParam(entry.getKey(), entry.getValue());
        }
        try {
            Response response = requestBuilder.execute().get();
            return response;
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return null;
            }

            logger.warn("Now retrying");
            return post(url, params, num + 1);
        }
    }


    /**
     * 发送post请求，带参数和headers
     * @param url
     * @param params
     * @param headers
     * @param num
     * @return
     */
    public String sendPostRequest(String url, Map<String, String> params, Map<String, String> headers, int num) {
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

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return null;
            }

            logger.warn("Now retrying");
            return sendPostRequest(url, params, num + 1);
        }
    }

    /**
     * 发送post请求，带参数和headers，返回response
     * @param url
     * @param params
     * @param headers
     * @param num
     * @return
     */
    public Response post(String url, Map<String, String> params, Map<String, String> headers, int num) {
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

            if (num >= systemConfig.getMaxRequestFailNum()) {
                logger.error("Too many failures, stop recursion");
                return null;
            }

            logger.warn("Now retrying");
            return post(url, params, num + 1);
        }
    }

}
