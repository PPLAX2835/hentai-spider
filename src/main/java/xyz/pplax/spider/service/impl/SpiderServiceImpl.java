package xyz.pplax.spider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.ArtistDao;
import xyz.pplax.spider.dao.PlatformArtistDao;
import xyz.pplax.spider.dao.PlatformDao;
import xyz.pplax.spider.model.constants.PlatformConstants;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.Platform;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.service.SpiderService;

import java.util.List;

@Service
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private PlatformArtistDao platformArtistDao;

    @Autowired
    private ArtistDao artistDao;


    /**
     * platformArtist 对应的图片
     * @param id
     * @return
     */
    @Override
    public void spiderByPlatformArtist(Long id) {

        // 判断所属平台
        PlatformArtist platformArtist = platformArtistDao.selectByPrimaryKey(id);
        Platform platform = platformDao.selectByPrimaryKey(platformArtist.getPlatformId());

        if (platform.getName().equals(PlatformConstants.E621)) {
            // 执行e621的爬虫

        }
        if (platform.getName().equals(PlatformConstants.RULE34)) {
            // 执行rule34的爬虫

        }
        if (platform.getName().equals(PlatformConstants.PIXIV)) {
            // 执行pixiv的爬虫

        }
    }

    /**
     * 下载该作者所有已知平台的
     * @param id
     * @return
     */
    @Override
    public void spiderByArtist(Long id) {
        List<PlatformArtist> platformArtistList = platformArtistDao.selectListByArtistId(id);

        for (PlatformArtist platformArtist : platformArtistList) {
            spiderByPlatformArtist(platformArtist.getId());
        }
    }
}
