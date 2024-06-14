package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.model.pojo.Config;
import xyz.pplax.spider.model.pojo.Platform;
import xyz.pplax.spider.service.ConfigService;
import xyz.pplax.spider.service.PlatformService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping(value = "")
    public String getList() {

        List<Config> configList = configService.listAll();
        if (configList.size() > 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS, configList));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PutMapping(value = "")
    public String save(
            @RequestParam(name = "basePath", required = false) String basePath,
            @RequestParam(name = "maxRequestFailNum", required = false) Integer maxRequestFailNum,
            @RequestParam(name = "proxyEnable", required = false) Boolean proxyEnable,
            @RequestParam(name = "proxyHost", required = false) String proxyHost,
            @RequestParam(name = "proxyPort", required = false) Integer proxyPort,
            @RequestParam(name = "userAgent", required = false) String userAgent,
            @RequestParam(name = "furaffinityEnableCookie", required = false) Boolean furaffinityEnableCookie,
            @RequestParam(name = "furaffinityCookie", required = false) String furaffinityCookie,
            @RequestParam(name = "pixivEnableCookie", required = false) Boolean pixivEnableCookie,
            @RequestParam(name = "pixivCookie", required = false) String pixivCookie
    ) {

        Config config = new Config();
        config.setBasePath(basePath);
        config.setMaxRequestFailNum(maxRequestFailNum);
        config.setProxyEnable(proxyEnable);
        config.setProxyHost(proxyHost);
        config.setProxyPort(proxyPort);
        config.setUserAgent(userAgent);
        config.setFuraffinityCookie(furaffinityCookie);
        config.setFuraffinityEnableCookie(furaffinityEnableCookie);
        config.setPixivEnableCookie(pixivEnableCookie);
        config.setPixivCookie(pixivCookie);

        int res = configService.save(config);

        if (res != 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
