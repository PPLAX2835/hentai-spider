package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.pojo.Config;

import java.util.List;

@Mapper
public interface ConfigDao {
    List<Config> selectAll();

    int insert(Config record);

    int insertSelective(Config record);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);

    int save(Config record);
}