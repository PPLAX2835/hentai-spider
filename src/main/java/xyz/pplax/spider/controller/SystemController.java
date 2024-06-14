package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.FailureHandler;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.web.bind.annotation.*;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.model.pojo.Config;
import xyz.pplax.spider.service.ConfigService;
import xyz.pplax.spider.spiders.E621Spider;

import java.util.List;

@RestController
@RequestMapping(value = "/api/system")
public class SystemController {

    private final static Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private ConfigService configService;


    @RequestMapping(value = "/restart")
    public String restart() {

        Restarter restarter = Restarter.getInstance();
        restarter.restart(new FailureHandler() {

            public Outcome handle(Throwable failure) {
                logger.error("当前系统出现问题，无法重启项目...........");
                return Outcome.ABORT;
            }

        });
        return "重启服务成功！";
    }
}
