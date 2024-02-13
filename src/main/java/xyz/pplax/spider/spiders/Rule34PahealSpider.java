package xyz.pplax.spider.spiders;

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
import xyz.pplax.spider.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Rule34PahealSpider {

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    private final static Logger logger = LoggerFactory.getLogger(Rule34PahealSpider.class);

    // 这个正则负责获得最大页码
    private String maxPageRegex = "<a\\s+href='([^']+)'>(Last)</a>";

    /**
     * 先获取这个平台作者的所有文件信息
     * @param platformArtist
     * @return
     */
    public List<File> getFileList(PlatformArtist platformArtist, Artist artist) {

        String homePageUrl = platformArtist.getHomepageUrl();
        int maxPage = Integer.MIN_VALUE;
        List<File> fileList = new ArrayList<>();

        // 获得html文本
        String responseString = asyncHttpUtil.sendGetRequest(homePageUrl);
        homePageUrl = homePageUrl.substring(0, homePageUrl.lastIndexOf("/"));

        // 编译正则表达式
        Pattern maxPageRegexPattern = Pattern.compile(maxPageRegex);

        // 获得最大页码
        Matcher matcher = maxPageRegexPattern.matcher(responseString);
        while (matcher.find()) {
            // 获取匹配的href属性值
            String href = matcher.group(1);

            // 提取最后一个路径
            String[] parts = href.split("/");
            String lastPath = parts[parts.length - 1];

            int page = Integer.parseInt(lastPath);
            if (page > maxPage) {
                maxPage = page;
            }
        }

        // 获得所有文件信息
        List<String> urlList = new ArrayList<>();
        for (int i = 2; i <= maxPage; i++) {
            urlList.add(homePageUrl + "/" + i);
        }
        // 批量进行请求
        CompletableFuture<List<String>> listCompletableFuture = asyncHttpUtil.sendGetRequestBatch(urlList);

        // 等待异步请求完成，并获取结果
        List<String> respList = listCompletableFuture.join();

        for (String resp : respList) {

            Document doc = Jsoup.parseBodyFragment(resp);

            Element body = doc.body();
            Elements elementsByClass = body.getElementsByClass("shm-thumb thumb");

            for (Element element : elementsByClass) {
                String type = element.attr("data-ext");
                String idInPlatform = element.attr("data-post-id");
                Elements a = element.select("a");
                String href = a.get(1).attr("href");

                // 封装，此时文件大小和hash值还没有
                File file = new File();
                file.setArtistId(platformArtist.getArtistId());
                file.setPlatformId(platformArtist.getPlatformId());
                file.setIdInPlatform(idInPlatform);
                file.setFileUrl(href);
                file.setFileType(type);
                file.setFileName(href.substring(href.lastIndexOf("/") + 1) + "." + type);
                file.setFilePath("/" + artist.getName() + "/");
                file.setPageUrl("https://rule34.paheal.net/post/view/" + idInPlatform);

                fileList.add(file);

            }

        }

        logger.info(String.format("数据抓取完成，总共%d个文件：", fileList.size()));

        return fileList;
    }

}
