package xyz.pplax.spider.service;

import xyz.pplax.spider.model.pojo.Platform;

import java.util.List;

public interface PlatformService {

    public Integer deleteById(Long id);

    List<Platform> getList();

}
