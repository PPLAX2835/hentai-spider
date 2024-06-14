package xyz.pplax.spider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.ArtistDao;
import xyz.pplax.spider.dao.FileDao;
import xyz.pplax.spider.dao.PlatformArtistDao;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.service.ArtistService;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistDao artistDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private PlatformArtistDao platformArtistDao;

    @Override
    public Integer count(String keyword) {
        return artistDao.selectCountByKeyword(keyword);
    }

    @Override
    public Integer add(Artist artist) {
        return artistDao.insertSelective(artist);
    }

    @Override
    public List<Artist> getPageByKeyword(int page, int limit, String keyword) {
        List<Artist> artistList = artistDao.selectPageByName((page - 1) * limit, limit, keyword);

        // 获得已下载作品总数
        for (Artist artist : artistList) {
            artist.setTotalCount(fileDao.selectTotalByArtistId(artist.getId()));
        }

        return artistList;
    }

    @Override
    public Integer updateSelectiveById(Artist artist) {
        return artistDao.updateByPrimaryKeySelective(artist);
    }

    @Override
    public Integer deleteById(Long id) {
        List<PlatformArtist> platformArtistList = platformArtistDao.selectListByArtistId(id);
        if (platformArtistList.size() > 0) {
            return 0;
        }
        List<File> files = fileDao.selectPageByArtistId(0, 1, id);
        if (files.size() > 0) {
            return 0;
        }

        return artistDao.deleteByPrimaryKey(id);
    }
}
