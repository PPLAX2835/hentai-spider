package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.pojo.ArtistHomepageUrl;

@Mapper
public interface ArtistHomepageUrlDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ArtistHomepageUrl record);

    int insertSelective(ArtistHomepageUrl record);

    ArtistHomepageUrl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArtistHomepageUrl record);

    int updateByPrimaryKey(ArtistHomepageUrl record);
}