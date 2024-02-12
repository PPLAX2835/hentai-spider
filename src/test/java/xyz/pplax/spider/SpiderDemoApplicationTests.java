package xyz.pplax.spider;

import com.alibaba.fastjson2.JSON;
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
    public void mapperTest01() {

//        Artist artist = new Artist();
//        artist.setName("testname");
//        artist.setInsertAt(new Date());
//        artist.setUpdateAt(new Date());
//
//        artistDao.insertSelective(artist);

        List<Artist> dne = artistDao.selectPageByName(0, 10, "oll");
        System.out.println(JSON.toJSONString(dne));

    }

    @Test
    void e621RegexTest() {
        String input = "<article id=\"post_4586064\" class=\"post-preview post-status-pending post-rating-explicit\" data-id=\"4586064\" data-has-sound=\"false\" data-tags=\"absurd_res big_eyes black_border bodily_fluids border erection eyelashes fellatio fellatio_pov female first_person_view gastly generation_1_pokemon genital_fluids genitals ghost hi_res male male/female male_penetrating nintendo not_furry oral oral_penetration penetration penile penis pokemon pokemon_(species) sex simple_background spirit text warlocke\" data-rating=\"e\" data-flags=\"pending\" data-uploader-id=\"349142\" data-uploader=\"warlocke\" data-file-ext=\"png\" data-score=\"4\" data-fav-count=\"5\" data-is-favorited=\"false\" data-file-url=\"https://static1.e621.net/data/08/02/0802f33553be5b764ae5a214cdab16b6.png\" data-large-file-url=\"https://static1.e621.net/data/sample/08/02/0802f33553be5b764ae5a214cdab16b6.jpg\" data-preview-file-url=\"https://static1.e621.net/data/preview/08/02/0802f33553be5b764ae5a214cdab16b6.jpg\"><a href=\"/posts/4586064\"><picture><source media=\"(max-width: 800px)\" srcset=\"https://static1.e621.net/data/crop/08/02/0802f33553be5b764ae5a214cdab16b6.jpg\"><source media=\"(min-width: 800px)\" srcset=\"https://static1.e621.net/data/preview/08/02/0802f33553be5b764ae5a214cdab16b6.jpg\"><img class=\"has-cropped-true\" src=\"https://static1.e621.net/data/preview/08/02/0802f33553be5b764ae5a214cdab16b6.jpg\" title=\"Rating: e\n" +
                "ID: 4586064\n" +
                "Date: 2024-02-08 09:25:26 -0500\n" +
                "Status: pending\n" +
                "Score: 4\n" +
                "\n" +
                "absurd_res big_eyes black_border bodily_fluids border erection eyelashes fellatio fellatio_pov female first_person_view gastly generation_1_pokemon genital_fluids genitals ghost hi_res male male/female male_penetrating nintendo not_furry oral oral_penetration penetration penile penis pokemon pokemon_(species) sex simple_background spirit text warlocke\" alt=\"absurd_res big_eyes black_border bodily_fluids border erection eyelashes fellatio fellatio_pov female first_person_view gastly generation_1_pokemon genital_fluids genitals ghost hi_res male male/female male_penetrating nintendo not_furry oral oral_penetration penetration penile penis pokemon pokemon_(species) sex simple_background spirit text warlocke\"></picture></a><div class=\"desc\"><div class=\"post-score\" id=\"post-score-4586064\"><span class=\"post-score-score score-positive\">↑4</span><span class=\"post-score-faves\">♥5</span><span class=\"post-score-comments\">C0</span><span class=\"post-score-rating\">E</span><span class=\"post-score-extras\">U</span></div></div></article>\n" +
                "        <article id=\"post_4586063\" class=\"post-preview post-status-pending post-status-has-parent post-rating-explicit\" data-id=\"4586063\" data-has-sound=\"false\" data-tags=\"avian beak bird blue_body bodily_fluids butterfingers cum cum_on_ground cumshot ejaculation erection eyes_closed falco_lombardi genital_fluids genitals hi_res macro male masturbation muscular muscular_male nintendo orgasm penis sitting solo star_fox\" data-rating=\"e\" data-flags=\"pending\" data-uploader-id=\"55942\" data-uploader=\"togepi1125\" data-file-ext=\"jpg\" data-score=\"1\" data-fav-count=\"1\" data-is-favorited=\"false\" data-file-url=\"https://static1.e621.net/data/8f/f0/8ff0d963a903da1bd8498372c5636c3a.jpg\" data-large-file-url=\"https://static1.e621.net/data/sample/8f/f0/8ff0d963a903da1bd8498372c5636c3a.jpg\" data-preview-file-url=\"https://static1.e621.net/data/preview/8f/f0/8ff0d963a903da1bd8498372c5636c3a.jpg\"><a href=\"/posts/4586063\"><picture><source media=\"(max-width: 800px)\" srcset=\"https://static1.e621.net/data/crop/8f/f0/8ff0d963a903da1bd8498372c5636c3a.jpg\"><source media=\"(min-width: 800px)\" srcset=\"https://static1.e621.net/data/preview/8f/f0/8ff0d963a903da1bd8498372c5636c3a.jpg\"><img class=\"has-cropped-true\" src=\"https://static1.e621.net/data/preview/8f/f0/8ff0d963a903da1bd8498372c5636c3a.jpg\" title=\"Rating: e\n" +
                "ID: 4586063\n" +
                "Date: 2024-02-08 09:25:06 -0500\n" +
                "Status: pending\n" +
                "Score: 1\n" +
                "\n" +
                "avian beak bird blue_body bodily_fluids butterfingers cum cum_on_ground cumshot ejaculation erection eyes_closed falco_lombardi genital_fluids genitals hi_res macro male masturbation muscular muscular_male nintendo orgasm penis sitting solo star_fox\" alt=\"avian beak bird blue_body bodily_fluids butterfingers cum cum_on_ground cumshot ejaculation erection eyes_closed falco_lombardi genital_fluids genitals hi_res macro male masturbation muscular muscular_male nintendo orgasm penis sitting solo star_fox\"></picture></a><div class=\"desc\"><div class=\"post-score\" id=\"post-score-4586063\"><span class=\"post-score-score score-positive\">↑1</span><span class=\"post-score-faves\">♥1</span><span class=\"post-score-comments\">C0</span><span class=\"post-score-rating\">E</span><span class=\"post-score-extras\">PU</span></div></div></article>\n" +
                "        <article id=\"post_4586062\" class=\"post-preview post-status-pending post-rating-questionable\" data-id=\"4586062\" data-has-sound=\"false\" data-tags=\"4_fingers abs anthro bracelet collar eyeless fingers hi_res horn jewelry jun_snowy male monster muscular muscular_male nipple_piercing nipples pecs piercing pumpkin_head skirt_only solo\" data-rating=\"q\" data-flags=\"pending\" data-uploader-id=\"1776620\" data-uploader=\"ZarkonEye\" data-file-ext=\"png\" data-score=\"0\" data-fav-count=\"0\" data-is-favorited=\"false\" data-file-url=\"https://static1.e621.net/data/3e/6c/3e6cca017f2698fb57d07662e97c38a6.png\" data-large-file-url=\"https://static1.e621.net/data/sample/3e/6c/3e6cca017f2698fb57d07662e97c38a6.jpg\" data-preview-file-url=\"https://static1.e621.net/data/preview/3e/6c/3e6cca017f2698fb57d07662e97c38a6.jpg\"><a href=\"/posts/4586062\"><picture><source media=\"(max-width: 800px)\" srcset=\"https://static1.e621.net/data/crop/3e/6c/3e6cca017f2698fb57d07662e97c38a6.jpg\"><source media=\"(min-width: 800px)\" srcset=\"https://static1.e621.net/data/preview/3e/6c/3e6cca017f2698fb57d07662e97c38a6.jpg\"><img class=\"has-cropped-true\" src=\"https://static1.e621.net/data/preview/3e/6c/3e6cca017f2698fb57d07662e97c38a6.jpg\" title=\"Rating: q\n" +
                "ID: 4586062\n" +
                "Date: 2024-02-08 09:24:50 -0500\n" +
                "Status: pending\n" +
                "Score: 0\n" +
                "\n" +
                "4_fingers abs anthro bracelet collar eyeless fingers hi_res horn jewelry jun_snowy male monster muscular muscular_male nipple_piercing nipples pecs piercing pumpkin_head skirt_only solo\" alt=\"4_fingers abs anthro bracelet collar eyeless fingers hi_res horn jewelry jun_snowy male monster muscular muscular_male nipple_piercing nipples pecs piercing pumpkin_head skirt_only solo\"></picture></a><div class=\"desc\"><div class=\"post-score\" id=\"post-score-4586062\"><span class=\"post-score-score score-neutral\">↕0</span><span class=\"post-score-faves\">♥0</span><span class=\"post-score-comments\">C0</span><span class=\"post-score-rating\">Q</span><span class=\"post-score-extras\">U</span></div></div></article>\n" +
                "        <article id=\"post_4586061\" class=\"post-preview post-status-pending post-status-has-children post-rating-explicit\" data-id=\"4586061\" data-has-sound=\"false\" data-tags=\"anthro avian beak bird blue_body bodily_fluids butterfingers erection falco_lombardi genital_fluids genitals hi_res macro male masturbation muscular muscular_male nintendo penis precum sitting solo star_fox\" data-rating=\"e\" data-flags=\"pending\" data-uploader-id=\"55942\" data-uploader=\"togepi1125\" data-file-ext=\"jpg\" data-score=\"0\" data-fav-count=\"0\" data-is-favorited=\"false\" data-file-url=\"https://static1.e621.net/data/a1/65/a16564aa342f2d5d3e6eb994bea5d459.jpg\" data-large-file-url=\"https://static1.e621.net/data/sample/a1/65/a16564aa342f2d5d3e6eb994bea5d459.jpg\" data-preview-file-url=\"https://static1.e621.net/data/preview/a1/65/a16564aa342f2d5d3e6eb994bea5d459.jpg\"><a href=\"/posts/4586061\"><picture><source media=\"(max-width: 800px)\" srcset=\"https://static1.e621.net/data/crop/a1/65/a16564aa342f2d5d3e6eb994bea5d459.jpg\"><source media=\"(min-width: 800px)\" srcset=\"https://static1.e621.net/data/preview/a1/65/a16564aa342f2d5d3e6eb994bea5d459.jpg\"><img class=\"has-cropped-true\" src=\"https://static1.e621.net/data/preview/a1/65/a16564aa342f2d5d3e6eb994bea5d459.jpg\" title=\"Rating: e\n" +
                "ID: 4586061\n" +
                "Date: 2024-02-08 09:24:31 -0500\n" +
                "Status: pending\n" +
                "Score: 0\n" +
                "\n" +
                "anthro avian beak bird blue_body bodily_fluids butterfingers erection falco_lombardi genital_fluids genitals hi_res macro male masturbation muscular muscular_male nintendo penis precum sitting solo star_fox\" alt=\"anthro avian beak bird blue_body bodily_fluids butterfingers erection falco_lombardi genital_fluids genitals hi_res macro male masturbation muscular muscular_male nintendo penis precum sitting solo star_fox\"></picture></a><div class=\"desc\"><div class=\"post-score\" id=\"post-score-4586061\"><span class=\"post-score-score score-neutral\">↕0</span><span class=\"post-score-faves\">♥0</span><span class=\"post-score-comments\">C0</span><span class=\"post-score-rating\">E</span><span class=\"post-score-extras\">CU</span></div></div></article>";

        String regex = "<article id=\"post_(\\d+)\"[^>]*data-file-url=\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String dataId = matcher.group(1);
            String dataFileUrl = matcher.group(2);
            System.out.println("data-id: " + dataId);
            System.out.println("data-file-url: " + dataFileUrl);
        }
    }

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;
    @Test
    public void e621Test() {
        String s = asyncHttpUtil.sendGetRequest("https://e621.net");

        System.out.println(s);
    }

    @Autowired
    private E621Spider e621Spider;
    @Test
    public void e621SpiderTest() {
        PlatformArtist platformArtist = new PlatformArtist();
        platformArtist.setHomepageUrl("https://e621.net/posts?tags=sollyz");

        Artist artist = new Artist();
        artist.setName("Sollyz");

        List<File> fileList = e621Spider.getFileList(platformArtist, artist);
        System.out.println(JSON.toJSONString(fileList));
    }

    @Test
    public void batchReqTest() {
        List<String> urls = new ArrayList<>();

        urls.add("https://e621.net/posts?page=1&tags=sollyz");
        urls.add("https://e621.net/posts?page=2&tags=sollyz");
        urls.add("https://e621.net/posts?page=3&tags=sollyz");
        urls.add("https://e621.net/posts?page=4&tags=sollyz");
        urls.add("https://e621.net/posts?page=5&tags=sollyz");

        CompletableFuture<List<String>> listCompletableFuture = asyncHttpUtil.sendGetRequestBatch(urls);

        // 等待异步请求完成，并获取结果
        List<String> resultList = listCompletableFuture.join();

        System.out.println(resultList.size());
    }

    @Test
    public void downloadBachTest() throws IOException {
        File file = new File();
        file.setArtistId(9L);
        file.setPlatformId(1L);
        file.setFileName("e621-761bb4b9631ba12548cfe65532d60b0f.png");
        file.setFilePath("/Sollyz/");
        file.setFileType("png");
        file.setFileUrl("https://static1.e621.net/data/76/1b/761bb4b9631ba12548cfe65532d60b0f.png");
        file.setIdInPlatform("331239");
        file.setPageUrl("https://e621.net/posts/331239");


        asyncHttpUtil.download(file);
    }

}
