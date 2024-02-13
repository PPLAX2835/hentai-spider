package xyz.pplax.spider.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.pplax.spider.model.pojo.File;

import java.util.List;

@Mapper
public interface FileDao {
    int deleteByPrimaryKey(Long id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Long id);

    /**
     * 通过平台id和在这个平台的id查询
     * @param platformId
     * @param idInPlatform
     * @return
     */
    File selectByPlatformIdAndIdInPlatform(@Param("platformId") Long platformId, @Param("idInPlatform") String idInPlatform);

    List<File> selectPageByArtistId(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("id") Long id);

    List<File> selectPageByPlatformId(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("id") Long id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);
}