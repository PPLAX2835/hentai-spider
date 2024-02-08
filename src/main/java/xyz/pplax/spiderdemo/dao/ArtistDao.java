package xyz.pplax.spiderdemo.dao;

import xyz.pplax.spiderdemo.model.pojo.Artist;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArtistDao {
    int deleteByPrimaryKey(Long id);

    int insert(Artist record);

    int insertSelective(Artist record);

    Artist selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Artist record);

    int updateByPrimaryKey(Artist record);
}