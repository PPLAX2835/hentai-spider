package xyz.pplax.spider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.pplax.spider.dao.FileDao;
import xyz.pplax.spider.dao.PlatformArtistDao;
import xyz.pplax.spider.dao.PlatformDao;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.model.vo.PlatformArtistVO;
import xyz.pplax.spider.service.PlatformArtistService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformArtistServiceImpl implements PlatformArtistService {

    @Autowired
    private PlatformArtistDao platformArtistDao;

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private FileDao fileDao;

    @Override
    public List<PlatformArtist> getListByArtistId(Long id) {
        List<PlatformArtist> platformArtistList = platformArtistDao.selectListByArtistId(id);

        // 封装平台实体
        for (PlatformArtist platformArtist : platformArtistList) {
            platformArtist.setPlatform(platformDao.selectByPrimaryKey(platformArtist.getPlatformId()));
        }

        return platformArtistList;
    }

    @Override
    public List<PlatformArtistVO> getVoListByArtistId(Long id) {
        List<PlatformArtist> platformArtistList = getListByArtistId(id);

        List<PlatformArtistVO> platformArtistVoList = new ArrayList<>();

        for (PlatformArtist platformArtist : platformArtistList) {
            // 封装
            PlatformArtistVO platformArtistVO = new PlatformArtistVO();
            platformArtistVO.setPlatformArtistName(platformArtist.getName());
            platformArtistVO.setHomepageUrl(platformArtist.getHomepageUrl());
            platformArtistVO.setPlatformName(platformArtist.getPlatform().getName());
            platformArtistVO.setPlatformUrl(platformArtist.getPlatform().getUrl());
            platformArtistVO.setArtistId(platformArtist.getArtistId());
            platformArtistVO.setPlatformArtistId(platformArtist.getId());
            platformArtistVO.setPlatformId(platformArtist.getPlatformId());
            platformArtistVO.setIdInPlatform(platformArtist.getIdInPlatform());
            platformArtistVO.setCount(fileDao.selectTotalByArtistIdAndPlatformId(platformArtist.getArtistId(), platformArtist.getPlatformId()));

            platformArtistVoList.add(platformArtistVO);
        }

        return platformArtistVoList;
    }

    @Override
    public Integer updateSelectiveById(PlatformArtist platformArtist) {
        return platformArtistDao.updateByPrimaryKeySelective(platformArtist);
    }

    @Override
    public Integer insertSelective(PlatformArtist platformArtist) {
        return platformArtistDao.insertSelective(platformArtist);
    }

    @Override
    public Integer deleteById(Long id) {
        return platformArtistDao.deleteByPrimaryKey(id);
    }

}
