package xyz.pplax.spiderdemo.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spiderdemo.model.pojo.File;

@Mapper
public interface FileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);
}