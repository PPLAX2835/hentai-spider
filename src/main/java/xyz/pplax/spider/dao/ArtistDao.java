package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.pplax.spider.model.pojo.Artist;

import java.util.List;

@Mapper
public interface ArtistDao {
    int deleteByPrimaryKey(Long id);

    int insert(Artist record);

    int insertSelective(Artist record);

    int selectCountByKeyword(@Param("keyword") String keyword);

    Artist selectByPrimaryKey(Long id);

    List<Artist> selectPage(@Param("limit") Integer limit, @Param("offset") Integer offset);

    List<Artist> selectPageByName(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("name") String name);

    int updateByPrimaryKeySelective(Artist record);

    int updateByPrimaryKey(Artist record);
}