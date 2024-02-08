package xyz.pplax.spiderdemo.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spiderdemo.model.pojo.Platform;

@Mapper
public interface PlatformDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Platform record);

    int insertSelective(Platform record);

    Platform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Platform record);

    int updateByPrimaryKey(Platform record);
}