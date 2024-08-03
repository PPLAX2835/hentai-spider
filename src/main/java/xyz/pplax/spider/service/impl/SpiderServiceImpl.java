package xyz.pplax.spider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.ArtistDao;
import xyz.pplax.spider.dao.PlatformArtistDao;
import xyz.pplax.spider.dao.PlatformDao;
import xyz.pplax.spider.model.constants.PlatformConstants;
import xyz.pplax.spider.model.pojo.*;
import xyz.pplax.spider.service.SpiderService;
import xyz.pplax.spider.spiders.*;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpiderServiceImpl implements SpiderService {

    private final static Logger logger = LoggerFactory.getLogger(SpiderServiceImpl.class);

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private PlatformArtistDao platformArtistDao;

    @Autowired
    private ArtistDao artistDao;

    @Autowired
    private E621Spider e621Spider;

    @Autowired
    private Rule34PahealSpider rule34PahealSpider;

    @Autowired
    private Rule34UsSpider rule34UsSpider;

    @Autowired
    private PixivSpider pixivSpider;

    @Autowired
    private FurAffinitySpider furAffinitySpider;

    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    @Autowired
    private Config systemConfig;

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

        // 文件列表
        List<File> fileList = null;

        if (platform.getName().equals(PlatformConstants.E621)) {
            // 执行e621的爬虫

            // 获得文件列表
            fileList = e621Spider.getFileList(platformArtist, artist);

            // 下载
            asyncHttpUtil.downloadBatch(fileList);
        }
        if (platform.getName().equals(PlatformConstants.RULE34_PAHEAL)) {
            // 执行rule34_paheal的爬虫

            // 获得文件列表
            fileList = rule34PahealSpider.getFileList(platformArtist, artist);

            // 下载
            asyncHttpUtil.downloadBatch(fileList);
        }
        if (platform.getName().equals(PlatformConstants.RULE34_US)) {
            // 执行rule34_us的爬虫

            // 获得文件列表
            fileList = rule34UsSpider.getFileList(platformArtist, artist);

            // 设置请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", systemConfig.getUserAgent());

            // 下载
            asyncHttpUtil.downloadBatch(fileList, headers);
        }
        if (platform.getName().equals(PlatformConstants.PIXIV)) {
            // 执行pixiv的爬虫

            try {
                // 获得文件列表
                fileList = pixivSpider.getFileList(platformArtist, artist);

                // 设置请求头
                Map<String, String> headers = new HashMap<>();
                headers.put("Referer", "https://www.pixiv.net/users/" + platformArtist.getIdInPlatform() + "/illustrations");

                // 下载
                asyncHttpUtil.downloadBatch(fileList, headers);

            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            }

        }
        if (platform.getName().equals(PlatformConstants.FURAFFINITY)) {
            // 执行furaffinity的爬虫

            // 获得文件列表
            fileList = furAffinitySpider.getFileList(platformArtist, artist);

            // 下载
            asyncHttpUtil.downloadBatch(fileList);

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

    /**
     * 抓取所有的
     */
    @Override
    public void spiderAll() {
        List<Artist> artistList = artistDao.selectAll();

        for (Artist artist : artistList) {
            spiderByArtist(artist.getId());
        }
    }
}
