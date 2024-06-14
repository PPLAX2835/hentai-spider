package xyz.pplax.spider.service;

import xyz.pplax.spider.model.pojo.Artist;

import java.util.List;

public interface ArtistService {

    public Integer add(Artist artist);

    public Integer count(String keyword);

    public List<Artist> getPageByKeyword(int page, int limit, String keyword);

    public Integer updateSelectiveById(Artist artist);

    public Integer deleteById(Long id);
}
