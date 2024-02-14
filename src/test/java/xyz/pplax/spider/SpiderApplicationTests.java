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
import xyz.pplax.spider.model.pojo.File;
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
    public void cookieTest() {
        String url = "https://www.pixiv.net/ajax/user/62046038/profile/all";

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "first_visit_datetime_pc=2023-05-09+10%3A13%3A17; p_ab_id=5; p_ab_id_2=5; p_ab_d_id=1505249353; yuid_b=QRhnhiI; c_type=21; privacy_policy_notification=0; a_type=0; b_type=1; _gid=GA1.2.1633827406.1707887089; cc1=2024-02-14%2015%3A18%3A50; privacy_policy_agreement=6; _ga_MZ1NL4PHH0=GS1.1.1707891536.1.0.1707891539.0.0.0; PHPSESSID=81693513_AW8aXl3lDQU1DWOqNdOQ8P2BsKF2z4Yn; device_token=7beeba884f4d4273bf99a3bf010b681c; QSI_S_ZN_5hF4My7Ad6VNNAi=v:0:0; login_ever=yes; __cf_bm=Qa29TAXO0OtGWGMjiqgMLPcEZ5NYYFLDunNaMUMRRCQ-1707892499-1-AQMJ72ZyKvtcJcIpFAAfwZKJajVwPXJOVkCPpFEG6Lno2v6iqDtkA6drABLZvdXcO2B9cKAWmNBnpShUw6X5HvpZSkH6XIBa663KLDucZIUu; cf_clearance=ZI3x7mkHhPgMwdA579iVJbE6cuUxIv0E1quoTRzcMlE-1707893439-1-AYMv2v5HL6QQ56jKBM6x/EvgeXHdmSOj8kawFQU+TnKc3jMm8u/iCoLxbepF+L2AW25ttaJ7WIbkM4ix8YeNXc0=; _ga_75BBYNYN9J=GS1.1.1707893436.3.1.1707893455.0.0.0; _ga=GA1.2.1473953209.1707887085");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0");
        String s = asyncHttpUtil.sendGetRequest(url, headers);
        System.out.println(s);

    }

    @Test
    public void limitImgTest() {
        String url = "https://www.pixiv.net/ajax/illust/110551710";

        List<String> urls = new ArrayList<>();
        urls.add("https://www.pixiv.net/ajax/illust/110551710");
        urls.add("https://www.pixiv.net/ajax/illust/110551710");
        urls.add("https://www.pixiv.net/ajax/illust/110551710");
        urls.add("https://www.pixiv.net/ajax/illust/110551710");
        urls.add("https://www.pixiv.net/ajax/illust/110551710");
        urls.add("https://www.pixiv.net/ajax/illust/110551710");
        urls.add("https://www.pixiv.net/ajax/illust/110551710");
        urls.add("https://www.pixiv.net/ajax/illust/110551710");

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "first_visit_datetime_pc=2023-05-09+10%3A13%3A17; p_ab_id=5; p_ab_id_2=5; p_ab_d_id=1505249353; yuid_b=QRhnhiI; c_type=21; privacy_policy_notification=0; a_type=0; b_type=1; _gid=GA1.2.1633827406.1707887089; cc1=2024-02-14%2015%3A18%3A50; privacy_policy_agreement=6; _ga_MZ1NL4PHH0=GS1.1.1707891536.1.0.1707891539.0.0.0; PHPSESSID=81693513_AW8aXl3lDQU1DWOqNdOQ8P2BsKF2z4Yn; device_token=7beeba884f4d4273bf99a3bf010b681c; QSI_S_ZN_5hF4My7Ad6VNNAi=v:0:0; login_ever=yes; __cf_bm=Qa29TAXO0OtGWGMjiqgMLPcEZ5NYYFLDunNaMUMRRCQ-1707892499-1-AQMJ72ZyKvtcJcIpFAAfwZKJajVwPXJOVkCPpFEG6Lno2v6iqDtkA6drABLZvdXcO2B9cKAWmNBnpShUw6X5HvpZSkH6XIBa663KLDucZIUu; cf_clearance=ZI3x7mkHhPgMwdA579iVJbE6cuUxIv0E1quoTRzcMlE-1707893439-1-AYMv2v5HL6QQ56jKBM6x/EvgeXHdmSOj8kawFQU+TnKc3jMm8u/iCoLxbepF+L2AW25ttaJ7WIbkM4ix8YeNXc0=; _ga_75BBYNYN9J=GS1.1.1707893436.3.1.1707893455.0.0.0; _ga=GA1.2.1473953209.1707887085");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0");
        List<String> strings = asyncHttpUtil.sendGetRequestBatch(urls, headers);

        for (String s : strings) {
            System.out.println(s);
        }
    }


}
