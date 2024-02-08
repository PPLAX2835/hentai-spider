package xyz.pplax.spider.dao.vo;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spider.model.dto.PlatformArtistVoDto;
import xyz.pplax.spider.model.vo.PlatformArtistVo;

import java.util.List;

@Mapper
public interface PlatformArtistVoDao {

    public List<PlatformArtistVo> selectByPlatformArtistVoDto(PlatformArtistVoDto platformArtistVoDto);

}