package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.service.PlatformArtistService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/platformArtist")
public class PlatformArtistController {

    @Autowired
    private PlatformArtistService platformArtistService;

    @GetMapping(value = "/{id}")
    public String getListByArtistId(@PathVariable("id") Long id) {
        List<PlatformArtist> platformArtistList = platformArtistService.getListByArtistId(id);

        return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS, platformArtistList.size(), platformArtistList));
    }

}
