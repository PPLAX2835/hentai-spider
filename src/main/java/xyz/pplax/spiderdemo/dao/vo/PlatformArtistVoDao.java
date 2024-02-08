package xyz.pplax.spiderdemo.dao.vo;

import org.apache.ibatis.annotations.Mapper;
import xyz.pplax.spiderdemo.model.dto.PlatformArtistVoDto;
import xyz.pplax.spiderdemo.model.vo.PlatformArtistVo;

import java.util.List;

@Mapper
public interface PlatformArtistVoDao {

    public List<PlatformArtistVo> selectByPlatformArtistVoDto(PlatformArtistVoDto platformArtistVoDto);

}