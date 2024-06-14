package xyz.pplax.spider.service;

import xyz.pplax.spider.model.pojo.Platform;

import java.util.List;

public interface PlatformService {

    public Integer deleteById(Long id);

    List<Platform> getList();

    Integer count();

    Integer add(Platform platform);

    Integer updateSelectiveById(Platform platform);

}
