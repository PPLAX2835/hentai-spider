package xyz.pplax.spider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.PlatformArtistDao;
import xyz.pplax.spider.dao.PlatformDao;
import xyz.pplax.spider.model.pojo.Platform;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.service.PlatformArtistService;

import java.util.List;

@Service
public class PlatformArtistServiceImpl implements PlatformArtistService {

    @Autowired
    private PlatformArtistDao platformArtistDao;

    @Autowired
    private PlatformDao platformDao;

    @Override
    public List<PlatformArtist> getListByArtistId(Long id) {
        List<PlatformArtist> platformArtistList = platformArtistDao.selectListByArtistId(id);

        // 封装平台实体
        for (PlatformArtist platformArtist : platformArtistList) {
            platformArtist.setPlatform(platformDao.selectByPrimaryKey(platformArtist.getPlatformId()));
        }

        return platformArtistList;
    }
}
