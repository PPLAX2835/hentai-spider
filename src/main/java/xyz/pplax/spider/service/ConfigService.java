package xyz.pplax.spider.service;

import xyz.pplax.spider.model.pojo.Config;

import java.util.List;

public interface ConfigService {

    public List<Config> listAll();

    public int add(Config record);

    public int addSelective(Config record);

    public int updateSelectiveById(Config record);

    public int updateById(Config record);

    public int save(Config record);

}
