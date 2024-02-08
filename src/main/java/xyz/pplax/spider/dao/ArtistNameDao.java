package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.pojo.ArtistName;

@Mapper
public interface ArtistNameDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ArtistName record);

    int insertSelective(ArtistName record);

    ArtistName selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArtistName record);

    int updateByPrimaryKey(ArtistName record);
}