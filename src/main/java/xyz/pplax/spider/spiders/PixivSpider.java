package xyz.pplax.spider.spiders;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PixivSpider {


    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    @Autowired
    private Executor threadPoolTaskExecutor;

    private final static Logger logger = LoggerFactory.getLogger(PixivSpider.class);


    /**
     * 先获取这个平台作者的所有文件信息
     * @param platformArtist
     * @return
     */
    public List<File> getFileList(PlatformArtist platformArtist, Artist artist) throws JsonProcessingException {

        List<File> fileList = new ArrayList<>();

        // 先获取所有的作品id
        logger.info("正在获取所有作品id");
        String profileGetUrl = "https://www.pixiv.net/ajax/user/" + platformArtist.getIdInPlatform() + "/profile/all";

        String profileRespJson = asyncHttpUtil.sendGetRequest(profileGetUrl);

        // 处理
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(profileRespJson, Map.class);
        Map body = (Map) map.get("body");
        Map illusts = (Map) body.get("illusts");
        Set<String> illustsSet = illusts.keySet();
        // 最终的作品id列表
        List<String> illustsIdList = new ArrayList<>(illustsSet);
        logger.info(String.format("获取成功，共%d条结果", illustsIdList.size()));

        int step = 0;
        String url = "https://www.pixiv.net/ajax/user/" + platformArtist.getIdInPlatform() + "/profile/illusts?";
        List<String> workDetailGetUrls = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer(url);                  // 就用这个，毕竟多线程任务，怕出错
        for (String illustId : illustsIdList) {
            if (step == 9) {
                stringBuffer.append("work_category=illust&is_first_page=1");        // 这两个参数是必要的
                workDetailGetUrls.add(stringBuffer.toString());
                stringBuffer = new StringBuffer(url);                   // 重置
                step = 0;
            } else {
                stringBuffer.append("ids[]=").append(illustId).append("&");
            }
            step++;
        }

        // 通过这些地址请求获得响应结果
        List<String> worksRespJsonList = asyncHttpUtil.sendGetRequestBatch(workDetailGetUrls);
        // 处理
        for (String woksRespJson : worksRespJsonList){
            ObjectMapper worksRespJsonObjectMapper = new ObjectMapper();
            Map worksRespJsonObjectMapperMap = worksRespJsonObjectMapper.readValue(woksRespJson, Map.class);
            Map worksRespJsonBody = (Map) worksRespJsonObjectMapperMap.get("body");
            Map works = (Map) worksRespJsonBody.get("works");

            System.out.println(JSON.toJSONString(works));

            Set<String> set = works.keySet();
            for (String key : set) {
                Map workMap = (Map) works.get(key);
                String fileUrl = (String) workMap.get("url");
                // 封装file
                File file = new File();
                file.setArtistId(platformArtist.getArtistId());
                file.setPlatformId(platformArtist.getPlatformId());
                file.setIdInPlatform((String) workMap.get("id"));
                file.setFileUrl(fileUrl);
                file.setFileType(fileUrl.substring(fileUrl.lastIndexOf(".") + 1));
                file.setFileName(fileUrl.substring(fileUrl.lastIndexOf("/") + 1));
                file.setFilePath("/" + artist.getName() + "/");
                file.setPageUrl("https://www.pixiv.net/artworks/" + workMap.get("id"));

                fileList.add(file);
            }
        }

        return setFileUrls(fileList);
    }


    public List<File> setFileUrls(List<File> fileList) {

        List<CompletableFuture<File>> fileCompletableFutureList = new ArrayList<>();
        // 正则
        String regex = "\"original\":\"(.*?)\"},\"";
        Pattern pattern = Pattern.compile(regex);


        for (File file : fileList) {

            // 提交任务
            CompletableFuture<File> fileCompletableFuture = CompletableFuture.supplyAsync(() -> {
                // 获得响应
                logger.info("正在获取详情页为：" + file.getPageUrl() + "的文件地址");

                String responseString = asyncHttpUtil.sendGetRequest(file.getPageUrl());

                // 通过正则获得文件地址
                String fileUrl = null;
                Matcher matcher = pattern.matcher(responseString);
                if (matcher.find()) {
                    fileUrl = matcher.group(1);
                }

                // 获得文件路径、类型和文件名
                file.setFileUrl(fileUrl);
                file.setFileType(fileUrl.substring(fileUrl.lastIndexOf(".") + 1));
                file.setFileName("pixiv-" + file.getIdInPlatform() + "-" + file.getFileName() + "." + file.getFileType());

                return file;
            }, threadPoolTaskExecutor);

            fileCompletableFutureList.add(fileCompletableFuture);
        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(fileCompletableFutureList.toArray(new CompletableFuture[0]));


        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        CompletableFuture<List<File>> listCompletableFuture = allOf.thenApply(v ->
                fileCompletableFutureList.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );

        return listCompletableFuture.join();
    }


}
