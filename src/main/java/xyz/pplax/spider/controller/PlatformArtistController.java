package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.model.vo.PlatformArtistVO;
import xyz.pplax.spider.service.PlatformArtistService;

import java.util.List;

@RestController
@RequestMapping("/api/platformArtist")
public class PlatformArtistController {

    @Autowired
    private PlatformArtistService platformArtistService;

    @GetMapping(value = "/{id}")
    public String getListByArtistId(@PathVariable("id") Long id) {
        List<PlatformArtistVO> platformArtistVOList = platformArtistService.getVoListByArtistId(id);

        return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS, platformArtistVOList.size(), platformArtistVOList));
    }

    @PutMapping(value = "/{id}")
    public String update(
            @PathVariable("id") Long id,
            @RequestParam(name = "platformArtistName", required = false) String platformArtistName,
            @RequestParam(name = "platformUrl", required = false) String platformUrl,
            @RequestParam(name = "platformId", required = false) Long platformId) {

        PlatformArtist platformArtist = new PlatformArtist();
        platformArtist.setId(id);
        if (platformArtistName != null && !"".equals(platformArtistName)) {
            platformArtist.setName(platformArtistName);
        }
        if (platformUrl != null && !"".equals(platformUrl)) {
            platformArtist.setHomepageUrl(platformUrl);
        }
        if (platformId != null) {
            platformArtist.setPlatformId(platformId);
        }

        Integer res = platformArtistService.updateSelectiveById(platformArtist);

        if (res != 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable Long id) {

        Integer res = platformArtistService.deleteById(id);
        if (res > 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "删除失败"));
    }

}
