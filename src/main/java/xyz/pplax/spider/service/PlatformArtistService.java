package xyz.pplax.spider.service;

import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.model.vo.PlatformArtistVO;

import java.util.List;

public interface PlatformArtistService {

    public List<PlatformArtist> getListByArtistId(Long id);

    public List<PlatformArtistVO> getVoListByArtistId(Long id);

    public Integer updateSelectiveById(PlatformArtist platformArtist);

    public Integer deleteById(Long id);
}
