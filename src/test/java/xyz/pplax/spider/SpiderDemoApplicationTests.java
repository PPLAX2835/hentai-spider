package xyz.pplax.spider;

import com.alibaba.fastjson2.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.Platform;
import xyz.pplax.spider.dao.*;
//import xyz.pplax.spider.service.spider.FurAffinitySpiderService;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.spiders.E621Spider;
import xyz.pplax.spider.spiders.FurAffinitySpider;
import xyz.pplax.spider.spiders.Rule34PahealSpider;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class SpiderDemoApplicationTests {

    @Autowired
    private ArtistDao artistDao;


    @Test
    public void rule34RegexTest() {
//        String input = "<a href='/post/list/Sollyz/8'>Random</a> | <a href='/post/list/Sollyz/2'>Next</a> | <a href='/post/list/Sollyz/17'>Last</a><br />&lt;&lt; <b><a href='/post/list/Sollyz/1'>1</a></b> | <a href='/post/list/Sollyz/2'>2</a> | <a href='/post/list/Sollyz/3'>3</a> | <a href='/post/list/Sollyz/4'>4</a>";
//
//        // 定义正则表达式，匹配内部文本为 "Last" 的 <a> 标签的href属性值
//        Pattern pattern = Pattern.compile("<a\\s+href='([^']+)'>(Last)</a>");
//
//        // 创建 Matcher 对象
//        Matcher matcher = pattern.matcher(input);
//
//        // 查找匹配的部分
//        if (matcher.find()) {
//            // 获取匹配的href属性值
//            String href = matcher.group(1);
//
//            // 提取最后一个路径
//            String[] parts = href.split("/");
//            String lastPath = parts[parts.length - 1];
//
//            System.out.println("href属性值: " + href);
//            System.out.println("最后一个路径: " + lastPath);
//        }

        String input = "<div class='shm-thumb thumb' data-ext='png' data-tags='elden_ring ranni_the_witch sollyz' data-post-id='6169796'><a class='shm-thumb-link' href='/post/view/6169796'><img id='thumb_6169796' title='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 7.9MB // png\n" +
                "February 7, 2024; 10:01' alt='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 7.9MB // png\n" +
                "February 7, 2024; 10:01' height='192' width='128' src='/_thumbs/6f8ac00a5c713d4795ea34753b5f6d4d/thumb.jpg' loading='lazy' /></a><br /><a href='https://r34i.paheal-cdn.net/6f/8a/6f8ac00a5c713d4795ea34753b5f6d4d'>File Only</a><span class='need-del'> - <a href='#' onclick='image_hash_ban(6169796); return false;'>Ban</a></span></div><div class='shm-thumb thumb' data-ext='png' data-tags='elden_ring ranni_the_witch sollyz' data-post-id='6169795'><a class='shm-thumb-link' href='/post/view/6169795'><img id='thumb_6169795' title='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 7.9MB // png\n" +
                "February 7, 2024; 10:01' alt='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 7.9MB // png\n" +
                "February 7, 2024; 10:01' height='192' width='128' src='/_thumbs/4e9b31219f888f464687e504dd08cec4/thumb.jpg' loading='lazy' /></a><br /><a href='https://r34i.paheal-cdn.net/4e/9b/4e9b31219f888f464687e504dd08cec4'>File Only</a><span class='need-del'> - <a href='#' onclick='image_hash_ban(6169795); return false;'>Ban</a></span></div><div class='shm-thumb thumb' data-ext='png' data-tags='elden_ring ranni_the_witch sollyz' data-post-id='6169794'><a class='shm-thumb-link' href='/post/view/6169794'><img id='thumb_6169794' title='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 8.0MB // png\n" +
                "February 7, 2024; 10:01' alt='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 8.0MB // png\n" +
                "February 7, 2024; 10:01' height='192' width='128' src='/_thumbs/196f9494c9713a9bb7defcf9348b992a/thumb.jpg' loading='lazy' /></a><br /><a href='https://r34i.paheal-cdn.net/19/6f/196f9494c9713a9bb7defcf9348b992a'>File Only</a><span class='need-del'> - <a href='#' onclick='image_hash_ban(6169794); return false;'>Ban</a></span></div><div class='shm-thumb thumb' data-ext='png' data-tags='elden_ring ranni_the_witch sollyz' data-post-id='6169793'><a class='shm-thumb-link' href='/post/view/6169793'><img id='thumb_6169793' title='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 7.7MB // png\n" +
                "February 7, 2024; 10:01' alt='Elden_Ring Ranni_the_Witch Sollyz\n" +
                "3000x4500 // 7.7MB // png\n" +
                "February 7, 2024; 10:01' height='192' width='128' src='/_thumbs/a72f0b2fa0687aa2e5b13efb2bdbbcda/thumb.jpg' loading='lazy' /></a><br /><a href='https://r34i.paheal-cdn.net/a7/2f/a72f0b2fa0687aa2e5b13efb2bdbbcda'>File Only</a><span class='need-del'> - <a href='#' onclick='image_hash_ban(6169793); return false;'>Ban</a></span></div>";

        Document doc = Jsoup.parseBodyFragment(input);

        Element body = doc.body();
        Elements elementsByClass = body.getElementsByClass("shm-thumb thumb");

        for (Element element : elementsByClass) {
            System.out.println("*******");

            String ext = element.attr("data-ext");
            String id = element.attr("data-post-id");
            Elements a = element.select("a");
            String href = a.get(1).attr("href");

            System.out.println("ext:" + ext);
            System.out.println("id:" + id);
            System.out.println("href:" + href);

            System.out.println("*******");
        }

        // 好用，比正则好用多了

    }

    @Autowired
    private Rule34PahealSpider rule34PahealSpider;
    @Test
    public void rule34SpiderTest() {
        PlatformArtist platformArtist = new PlatformArtist();
        platformArtist.setHomepageUrl("https://rule34.paheal.net/post/list/Sollyz/1");

        rule34PahealSpider.getFileList(platformArtist, new Artist());
    }

}
