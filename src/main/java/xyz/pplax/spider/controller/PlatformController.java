package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.service.PlatformService;

@RestController
@RequestMapping(value = "/api/platform")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping(value = "")
    public String getList() {
        return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS, platformService.getList()));
    }

}
