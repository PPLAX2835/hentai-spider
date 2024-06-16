package xyz.pplax.spider.spiders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.dao.FileDao;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.Config;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class Rule34UsSpider {

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    @Autowired
    private Executor threadPoolTaskExecutor;

    @Autowired
    private Config systemConfig;

    private final static Logger logger = LoggerFactory.getLogger(Rule34UsSpider.class);

    /**
     * 获取单页的文件列表
     * @param platformArtist
     * @return
     */
    public List<File> getFileList(PlatformArtist platformArtist, Artist artist) {

        String homePageUrl = platformArtist.getHomepageUrl();
        int maxPage = 1;

        // 获得html文本
        Map<String, String > headers = new HashMap<>();
        headers.put("User-Agent", systemConfig.getUserAgent());
        String responseString = asyncHttpUtil.sendGetRequest(homePageUrl, headers, 0);

        // 获得最大页码
        Document doc = Jsoup.parseBodyFragment(responseString);
        Elements lastPageA = doc.getElementsByAttributeValue("alt", "last page");
        if (lastPageA.size() != 0) {
            String lastPageUrl = lastPageA.get(0).attr("href");
            maxPage = Integer.parseInt(lastPageUrl.substring(lastPageUrl.lastIndexOf("=") + 1));
        }

        // 获得每个页面的html文本
        List<String> urlList = new ArrayList<>();
        for (int i = 1; i <= maxPage; i++) {
            urlList.add(platformArtist.getHomepageUrl() + "&page=" + i);
        }
        List<String> responseStringList = asyncHttpUtil.sendGetRequestBatch(urlList, headers);
        responseStringList.add(responseString);


        // 遍历所有页面并提取内容
        List<File> fileList = new ArrayList<>();
        for (String respString : responseStringList) {
            Document document = Jsoup.parseBodyFragment(respString);

            Elements elementsByClass = document.getElementsByAttributeValue("style", "border-radius: 3px; margin: 0px 10px 15px 10px; overflow: hidden; height: 200px;");

            for (Element element : elementsByClass) {
                Elements a = element.select("a");
                Elements img = a.select("img");
                String idInPlatform = a.attr("id");

                String fileUrl = img.attr("src").replace("thumbnails", "images").replace("thumbnail_", "");

                File file = new File();

                file.setArtistId(platformArtist.getArtistId());
                file.setPlatformId(platformArtist.getPlatformId());
                file.setIdInPlatform(idInPlatform);
                file.setFileUrl(fileUrl);                           // 这个fileUrl有可能是错的，原因是这个路径是经过处理的获取的，但是文件后缀未知
                file.setFileType("");                               // 此时文件类型未知
                file.setFileName(fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.lastIndexOf(".")));
                file.setFilePath("/" + artist.getName() + "/");
                file.setPageUrl(a.attr("href"));

                fileList.add(file);
            }
        }

        List<File> result = getFileListByPageUrl(fileList).join();
        logger.info(String.format("数据抓取完成，总共%d个文件：", result.size()));

        return result;
    }


    /**
     * 因为列表页面没有文件的地址，需要进入到详情页获取
     * @param fileList
     * @return
     */
    public CompletableFuture<List<File>> getFileListByPageUrl(List<File> fileList) {

        List<CompletableFuture<File>> fileCompletableFutureList = new ArrayList<>();

        for (File file : fileList) {

            // 提交任务
            CompletableFuture<File> fileCompletableFuture = CompletableFuture.supplyAsync(() -> {
                // 获得响应
                logger.info("正在获取详情页为：" + file.getPageUrl() + "的文件地址");

                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", systemConfig.getUserAgent());
                String responseString = asyncHttpUtil.sendGetRequest(file.getPageUrl(), headers, 0);

                Document document = Jsoup.parseBodyFragment(responseString);

                Elements img = document.getElementsByAttributeValue("style", "height: auto; max-width: 100%;");
                String src = img.attr("src");

                if ("".equals(src)) {
                    // 说明这个不是图片资源，是视频资源
                    Elements video = document.getElementsByAttributeValue("style", "max-width: 100%; max-height: 80vh");

                    Element source = video.select("source").get(1);
                    src = source.attr("src");
                }

                // 获得文件路径、类型和文件名
                file.setFileUrl(src);
                file.setFileType(src.substring(src.lastIndexOf(".") + 1));
                file.setFileName("Rule34Us-" + file.getIdInPlatform() + "-" + file.getFileName() + "." + file.getFileType());

                return file;
            }, threadPoolTaskExecutor);

            fileCompletableFutureList.add(fileCompletableFuture);
        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(fileCompletableFutureList.toArray(new CompletableFuture[0]));


        // 当所有异步任务完成后，将它们的结果合并成一个List<String>并返回
        return allOf.thenApply(v ->
                fileCompletableFutureList.stream()
                        .map(CompletableFuture::join) // 获取各异步任务的结果
                        .collect(Collectors.toList()) // 将结果收集为List
        );
    }


}
