package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.service.ArtistService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping(value = "")
    public String getList(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<Artist> artistList = artistService.getPageByKeyword(page, limit, keyword);

        if (artistList == null || artistList.size() <= 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS, artistList.size(), artistList));
    }

    @PutMapping(value = "/{id}")
    public String update(@PathVariable Long id, @RequestParam(value = "name") String name) {

        Artist artist = new Artist();
        artist.setId(id);
        artist.setName(name);

        Integer res = artistService.updateSelectiveById(artist);

        if (res != 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
