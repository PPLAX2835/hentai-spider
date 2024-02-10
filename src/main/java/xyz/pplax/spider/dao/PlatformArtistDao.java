package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.pojo.PlatformArtist;

@Mapper
public interface PlatformArtistDao {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformArtist record);

    int insertSelective(PlatformArtist record);

    PlatformArtist selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformArtist record);

    int updateByPrimaryKey(PlatformArtist record);
}