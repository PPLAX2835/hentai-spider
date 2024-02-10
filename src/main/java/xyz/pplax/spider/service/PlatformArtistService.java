package xyz.pplax.spider.service;

import xyz.pplax.spider.model.pojo.PlatformArtist;

import java.util.List;

public interface PlatformArtistService {

    public List<PlatformArtist> getListByArtistId(Long id);

}
