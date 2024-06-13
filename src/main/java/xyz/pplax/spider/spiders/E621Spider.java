package xyz.pplax.spider.spiders;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.dao.FileDao;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.utils.AsyncHttpUtil;
import xyz.pplax.spider.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class E621Spider {

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    private final static Logger logger = LoggerFactory.getLogger(E621Spider.class);

    // 这个正则能够获取页面中的文件id和文件的url
    private String idAndUrlRegex = ".*?<article id=\"post_(\\d+)\"[^>]*data-file-url=\"([^\"]+)\".*?";

    // 这个正则负责获得最大页码
    private String maxPageRegex = "<li class=\"numbered-page\"><a[^>]+href=\"[^\"]*?\">(\\d+)</a></li>";

    /**
     * 先获取这个平台作者的所有文件信息
     * @param platformArtist
     * @return
     */
    public List<File> getFileList(PlatformArtist platformArtist, Artist artist) {
        String homePageUrl = platformArtist.getHomepageUrl();
        int maxPage = 1;
        List<File> fileList = new ArrayList<>();

        // 获得html文本
        String responseString = asyncHttpUtil.sendGetRequest(homePageUrl, 0);

        // 编译正则表达式
        Pattern idAndUrlPattern = Pattern.compile(idAndUrlRegex);
        Pattern maxPageRegexPattern = Pattern.compile(maxPageRegex);
        Matcher matcher = null;

        // 获得最大页码
        matcher = maxPageRegexPattern.matcher(responseString);
        while (matcher.find()) {
            int page = Integer.parseInt(matcher.group(1));
            if (page > maxPage) {
                maxPage = page;
            }
        }

        // 获得所有文件信息
        List<String> urlList = new ArrayList<>();
        for (int i = 1; i <= maxPage; i++) {
            urlList.add(homePageUrl + "&page=" + i);
        }

        // 批量进行请求，等待异步请求完成，并获取结果
        List<String> respList = asyncHttpUtil.sendGetRequestBatch(urlList);
        for (String resp : respList) {

            matcher = idAndUrlPattern.matcher(resp);

            while (matcher.find()) {
                // 封装，此时文件大小和hash值还没有
                File file = new File();
                file.setArtistId(platformArtist.getArtistId());
                file.setPlatformId(platformArtist.getPlatformId());
                file.setIdInPlatform(matcher.group(1));
                file.setFileUrl(matcher.group(2));
                file.setFileType(FileUtils.getFileExtension(matcher.group(2)));
                file.setFileName("e621-" + matcher.group(1) + "-" + matcher.group(2).substring(matcher.group(2).lastIndexOf("/") + 1));
                file.setFilePath("/" + artist.getName() + "/");
                file.setPageUrl("https://e621.net/posts/" + matcher.group(1));

                fileList.add(file);
            }
        }


        logger.info(String.format("数据抓取完成，总共%d个文件：", fileList.size()));

        return fileList;
    }

}
