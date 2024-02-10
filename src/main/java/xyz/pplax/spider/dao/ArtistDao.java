package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.pojo.Artist;

@Mapper
public interface ArtistDao {
    int deleteByPrimaryKey(Long id);

    int insert(Artist record);

    int insertSelective(Artist record);

    Artist selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Artist record);

    int updateByPrimaryKey(Artist record);
}