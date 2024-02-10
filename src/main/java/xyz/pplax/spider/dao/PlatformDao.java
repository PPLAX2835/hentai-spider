package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.pojo.Platform;

@Mapper
public interface PlatformDao {
    int deleteByPrimaryKey(Long id);

    int insert(Platform record);

    int insertSelective(Platform record);

    Platform selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Platform record);

    int updateByPrimaryKey(Platform record);
}