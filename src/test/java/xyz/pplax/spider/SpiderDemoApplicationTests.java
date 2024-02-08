package xyz.pplax.spider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import xyz.pplax.spider.dao.*;
import xyz.pplax.spider.dao.vo.PlatformArtistVoDao;
import xyz.pplax.spider.model.dto.PlatformArtistVoDto;
import xyz.pplax.spider.model.pojo.*;
import xyz.pplax.spider.model.vo.PlatformArtistVo;
import xyz.pplax.spider.service.FileService;
import xyz.pplax.spider.service.spider.FurAffinitySpiderService;
import xyz.pplax.spider.spiders.FurAffinitySpider;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class SpiderDemoApplicationTests {

    @Autowired
    AsyncHttpUtil asyncHttpUtil;
    @Autowired
    FurAffinitySpiderService furAffinitySpiderService;
    @Autowired
    FurAffinitySpider furAffinitySpider;
//    @Test
//    void contextLoads() throws Exception {
//
////        CompletableFuture<List<String>> sollyz = furAffinitySpiderService.getArtistIdListAsync("sollyz", 1, 20);
////
////
////        List<String> ids = sollyz.get();
////        System.out.println(ids);
////        System.out.println(ids.size());
//        String fileUrl = FurAffinitySpider.getFileUrl("30629403");
//        System.out.println(fileUrl);
//
//    }

    @Autowired
    PlatformDao platformDao;
    @Test
    void mapperTest1() throws Exception {

        Platform platform = new Platform();
        platform.setName("FurAffinity");
        platform.setUrl("https://www.furaffinity.net");
        platform.setInsertAt(new Date());
        platform.setUpdateAt(new Date());
        platformDao.insert(platform);
    }

    @Autowired
    ArtistDao artistDao;
    @Test
    void mapperTest02() throws Exception {

        Artist artist = new Artist();
        artist.setName("Sollyz");
        artist.setUpdateAt(new Date());
        artist.setInsertAt(new Date());
        artistDao.insert(artist);
    }

    @Autowired
    ArtistHomepageUrlDao artistHomepageUrlDao;
    @Test
    void mapperTest2() throws Exception {

        ArtistHomepageUrl artistHomepageUrl = new ArtistHomepageUrl();
        artistHomepageUrl.setHomepageUrl("https://www.furaffinity.net/user/sollyz/");
        artistHomepageUrl.setPlatformId(1);
        artistHomepageUrl.setArtistId(1L);
        artistHomepageUrl.setUpdateAt(new Date());
        artistHomepageUrl.setInsertAt(new Date());
        artistHomepageUrlDao.insert(artistHomepageUrl);
    }

    @Autowired
    ArtistNameDao artistNameDao;
    @Test
    void mapperTest3() throws Exception {

        ArtistName artistName = new ArtistName();
        artistName.setName("sollyz");
        artistName.setPlatformId(1);
        artistName.setArtistId(1L);
        artistName.setInsertAt(new Date());
        artistName.setUpdateAt(new Date());
        artistNameDao.insert(artistName);

    }

    @Autowired
    PlatformArtistVoDao platformArtistVoDao;
    @Test
    void PlatformArtistVoDaoTest() throws Exception {

        PlatformArtistVoDto platformArtistVoDto = new PlatformArtistVoDto();
        platformArtistVoDto.setPage(0);
        platformArtistVoDto.setSize(10);
        platformArtistVoDto.setPlatformName("FurAffinity");
        platformArtistVoDto.setArtistName("sollyz");

        List<PlatformArtistVo> platformArtistVos = platformArtistVoDao.selectByPlatformArtistVoDto(platformArtistVoDto);

        System.out.println(platformArtistVos);

    }

    @Autowired
    FileService fileService;
    @Test
    void getFileListByIdsTest() throws ExecutionException, InterruptedException {
        PlatformArtistVoDto platformArtistVoDto = new PlatformArtistVoDto();
        platformArtistVoDto.setPage(0);
        platformArtistVoDto.setSize(10);
        platformArtistVoDto.setPlatformName("FurAffinity");
        platformArtistVoDto.setArtistName("sollyz");

        List<PlatformArtistVo> platformArtistVos = platformArtistVoDao.selectByPlatformArtistVoDto(platformArtistVoDto);

        System.out.println(platformArtistVos);
        PlatformArtistVo platformArtistVo = platformArtistVos.get(0);

        CompletableFuture<List<String>> artistIdListCompletableFuture = furAffinitySpiderService.getArtistIdList(platformArtistVo.getArtistName());

        List<String> artistIdList = artistIdListCompletableFuture.get();

        System.out.println(artistIdList);

        System.out.println("getFileListByIdsTest");

        CompletableFuture<List<File>> fileListByIds = furAffinitySpiderService.getFileListByIds(artistIdList, platformArtistVo);
        List<File> files = fileListByIds.get();
        for (File file : files) {
            try {
                fileService.insertSelective(file);
            } catch (DuplicateKeyException e) {
                // 已经有的
                System.out.println(e.getMessage());
            }
            System.out.println(file);
        }
    }


}
