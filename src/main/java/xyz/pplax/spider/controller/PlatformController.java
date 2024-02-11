package xyz.pplax.spider.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.pplax.spider.model.http.HttpStatus;
import xyz.pplax.spider.model.http.ResponseResult;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.Platform;
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


    @PostMapping(value = "")
    public String add(@RequestParam(value = "name") String name, @RequestParam(value = "url") String url) {
        Platform platform = new Platform();

        platform.setName(name);
        platform.setUrl(url);

        Integer add = platformService.add(platform);
        if (add != 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PutMapping(value = "/{id}")
    public String update(@PathVariable Long id, @RequestParam(value = "name") String name, @RequestParam(value = "url") String url) {

        Platform platform = new Platform();
        platform.setId(id);
        platform.setName(name);
        platform.setUrl(url);

        Integer res = platformService.updateSelectiveById(platform);

        if (res != 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR));
    }


    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable Long id) {

        Integer res = platformService.deleteById(id);

        if (res > 0) {
            return JSON.toJSONString(new ResponseResult(HttpStatus.SUCCESS));
        }

        return JSON.toJSONString(new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "删除失败，该平台下可能还有其他数据"));
    }

}
