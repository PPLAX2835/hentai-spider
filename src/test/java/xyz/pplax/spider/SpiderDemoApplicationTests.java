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
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.spiders.E621Spider;
import xyz.pplax.spider.spiders.FurAffinitySpider;
import xyz.pplax.spider.spiders.Rule34PahealSpider;
import xyz.pplax.spider.spiders.Rule34UsSpider;
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


}
