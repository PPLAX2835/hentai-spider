package xyz.pplax.spider.spiders;

import com.alibaba.fastjson2.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.model.pojo.Artist;
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
public class FurAffinitySpider {

    private final static Logger logger = LoggerFactory.getLogger(FurAffinitySpider.class);

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    @Value("${pplax.spider.furaffinity.cookie:PPLAX}")
    private String cookie;

    @Value("${pplax.spider.furaffinity.enableCookie:false}")
    private boolean enableCookie;

    @Autowired
    private Executor threadPoolTaskExecutor;

    Map<String,String> headers = new HashMap<>();

    /**
     * 先获取这个平台作者的所有文件信息
     * @param platformArtist
     * @return
     */
    public List<File> getFileList(PlatformArtist platformArtist, Artist artist) {

        List<File> fileList = new ArrayList<>();
        // 添加请求头
        if (enableCookie) {
            headers.put("cookie", cookie);
        }

        // 获得所有作品主页
        int pageNum = 1;
        String galleryUrl = "https://www.furaffinity.net/gallery/" + platformArtist.getName() + "/";
        while (true) {
            String  galleryUrlPage = galleryUrl + pageNum;
            logger.info(String.format("正在获取第%d页的数据", pageNum));
            String respPage = asyncHttpUtil.sendGetRequest(galleryUrlPage, headers);


            Document document = Jsoup.parseBodyFragment(respPage);
            Elements figureList = document.select("figure");
            if (figureList.size() == 0) {
                break;
            }
            // 获得带有页面地址的a标签
            for (Element figure : figureList) {
                Element a = figure.select("a").get(0);
                String href = a.attr("href");
                // 封装作品主页地址
                File file = new File();
                file.setArtistId(platformArtist.getArtistId());
                file.setPlatformId(platformArtist.getPlatformId());
                file.setPageUrl("https://www.furaffinity.net" + href);
                file.setFilePath("/" + artist.getName() + "/");
                fileList.add(file);
            }

            pageNum++;
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

                String responseString = asyncHttpUtil.sendGetRequest(file.getPageUrl(), headers);

                Document document = Jsoup.parseBodyFragment(responseString);

                Elements div = document.getElementsByClass("download");
                Elements a = div.select("a");
                String src = a.attr("href");

                String tempStr = src.substring(src.lastIndexOf("/") + 1);
                String idInPlatform = tempStr.substring(0, tempStr.indexOf("."));

                // 获得文件路径、类型和文件名
                file.setFileUrl("https:" + src);
                file.setFileType(src.substring(src.lastIndexOf(".") + 1));
                file.setIdInPlatform(idInPlatform);
                file.setFileName("Furaffinity-" + idInPlatform + "." + file.getFileType());

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
