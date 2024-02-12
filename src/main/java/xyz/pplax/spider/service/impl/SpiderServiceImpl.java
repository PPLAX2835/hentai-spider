package xyz.pplax.spider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.ArtistDao;
import xyz.pplax.spider.dao.PlatformArtistDao;
import xyz.pplax.spider.dao.PlatformDao;
import xyz.pplax.spider.model.constants.PlatformConstants;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.Platform;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.service.SpiderService;
import xyz.pplax.spider.spiders.E621Spider;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.util.List;

@Service
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private PlatformArtistDao platformArtistDao;

    @Autowired
    private ArtistDao artistDao;

    @Autowired
    private E621Spider e621Spider;

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

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

        // 获得作者
        Artist artist = artistDao.selectByPrimaryKey(platformArtist.getArtistId());

        if (platform.getName().equals(PlatformConstants.E621)) {
            // 执行e621的爬虫

            // 获得文件列表
            List<File> fileList = e621Spider.getFileList(platformArtist, artist);

            // 下载
            asyncHttpUtil.downloadBatch(fileList);
        }
        if (platform.getName().equals(PlatformConstants.RULE34_PAHEAL)) {
            // 执行rule34_paheal的爬虫

        }
        if (platform.getName().equals(PlatformConstants.RULE34_US)) {
            // 执行rule34_us的爬虫

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
