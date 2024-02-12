package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.service.SpiderService;

@RestController
@RequestMapping(value = "/api/spider")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @Value("${pplax.spider.basepath}")
    private String basePath;

    /**
     * 只抓取某个平台的
     * @param id
     * @return
     */
    @RequestMapping(value = "/platformArtist/{id}")
    public void spiderByPlatformArtist(@PathVariable(value = "id") Long id) {

        spiderService.spiderByPlatformArtist(id);

    }

    /**
     * 抓取这个作者所有已知平台的
     * @param id
     * @return
     */
    @RequestMapping(value = "/artist/{id}")
    public void spiderByArtist(@PathVariable(value = "id") Long id) {

        spiderService.spiderByArtist(id);

    }

}
