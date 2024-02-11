package xyz.pplax.spider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.FileDao;
import xyz.pplax.spider.dao.PlatformArtistDao;
import xyz.pplax.spider.dao.PlatformDao;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.Platform;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.service.PlatformService;

import java.util.List;

@Service
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private PlatformArtistDao platformArtistDao;

    @Override
    public Integer deleteById(Long id) {

        List<PlatformArtist> platformArtistList = platformArtistDao.selectListByPlatformId(id);
        if (platformArtistList.size() > 0) {
            return 0;
        }

        List<File> files = fileDao.selectPageByPlatformId(0, 1, id);
        if (files.size() > 0) {
            return 0;
        }

        return platformDao.deleteByPrimaryKey(id);
    }

    /**
     * 因为平台就那几个，分页就没必要了
     * @return
     */
    @Override
    public List<Platform> getList() {
        return platformDao.selectAll();
    }
}
