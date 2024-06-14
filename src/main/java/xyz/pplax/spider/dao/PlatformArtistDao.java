package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.pojo.PlatformArtist;

import java.util.List;

@Mapper
public interface PlatformArtistDao {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformArtist record);

    int insertSelective(PlatformArtist record);

    int selectCount();

    PlatformArtist selectByPrimaryKey(Long id);

    List<PlatformArtist> selectListByArtistId(Long id);

    List<PlatformArtist> selectListByPlatformId(Long id);

    int updateByPrimaryKeySelective(PlatformArtist record);

    int updateByPrimaryKey(PlatformArtist record);
}