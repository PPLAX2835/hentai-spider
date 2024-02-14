package xyz.pplax.spider;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.spiders.PixivSpider;
import xyz.pplax.spider.utils.AsyncHttpUtil;
import xyz.pplax.spider.utils.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.*;

@SpringBootTest
class SpiderApplicationTests {

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    @Test
    public void profileListTest() throws JsonProcessingException {
        String url = "https://www.pixiv.net/ajax/user/490219/profile/all";

        String respJson = asyncHttpUtil.sendGetRequest(url);

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(respJson, Map.class);
        Map body = (Map) map.get("body");
        Map illusts = (Map) body.get("illusts");


        Set<String> illustsSet = illusts.keySet();
        List<String> illustsList = new ArrayList<>(illustsSet);

        System.out.println(respJson);
        System.out.println(map);
        System.out.println(body);
        System.out.println(illusts);
        System.out.println(illustsSet);
        System.out.println(illustsList);

    }

    @Test
    public void fileListTest() throws IOException {
        String[] ids = {"115329470", "114576085", "114556233", "114554183", "110834364", "110638612", "110597439", "107610529", "107610438", "107365069", "104180229", "103975128", "103920676", "103914558", "103912045", "103561543", "101091330", "100717441", "100715818", "100363203", "100339369", "98259515", "98056836", "98052048", "97610079", "95619535", "95024829", "95024779", "94993982", "94993840", "94819769", "93595847"};

        StringBuilder url = new StringBuilder("https://www.pixiv.net/ajax/user/490219/profile/illusts?");

        for (String id : ids) {
            url.append("ids[]=").append(id).append("&");
        }

        url.append("work_category=illust&is_first_page=1");


        String respJson = asyncHttpUtil.sendGetRequest(String.valueOf(url));

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(respJson, Map.class);
        Map body = (Map) map.get("body");
        Map works = (Map) body.get("works");

//        System.out.println(respJson);
//        System.out.println(map);
//        System.out.println(body);
//        System.out.println(works);

        Set<String> set = works.keySet();
        for (String key : set) {
            Map work = (Map) works.get(key);
            String workUrl = (String) work.get("url");

            Map<String, String> headers = new HashMap<>();
            headers.put("Referer", "https://www.pixiv.net/users/490219/illustrations");

            Response response = asyncHttpUtil.get(workUrl, headers);

            // 从响应中获取输入流
            ReadableByteChannel inputChannel = Channels.newChannel(response.getResponseBodyAsStream());

            // 创建目录
            FileUtils.createDirectory("G:\\tmp\\test");

            // 创建本地文件输出流
            FileOutputStream fos = new FileOutputStream("G:\\tmp\\test\\" + workUrl.substring(workUrl.lastIndexOf("/") + 1));
            FileChannel outputChannel = fos.getChannel();

            // 将输入流的内容写入本地文件
            outputChannel.transferFrom(inputChannel, 0, Long.MAX_VALUE);

            break;


        }
    }

    @Autowired
    private PixivSpider pixivSpider;

    @Test
    public void pixivSpiderTest() throws JsonProcessingException {
        PlatformArtist platformArtist = new PlatformArtist();
        Artist artist = new Artist();

        platformArtist.setIdInPlatform("490219");

        pixivSpider.getFileList(platformArtist, artist);
    }

}
