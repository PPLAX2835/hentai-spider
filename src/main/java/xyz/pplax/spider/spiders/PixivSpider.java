package xyz.pplax.spider.spiders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.model.pojo.Artist;
import xyz.pplax.spider.model.pojo.File;
import xyz.pplax.spider.model.pojo.PlatformArtist;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PixivSpider {


    @Autowired
    private AsyncHttpUtil asyncHttpUtil;

    private final static Logger logger = LoggerFactory.getLogger(PixivSpider.class);


    /**
     * 先获取这个平台作者的所有文件信息
     * @param platformArtist
     * @return
     */
    public List<File> getFileList(PlatformArtist platformArtist, Artist artist) throws JsonProcessingException {

        List<File> fileList = new ArrayList<>();

        // 先获取所有的作品id
        logger.info("正在获取所有作品id");
        String profileGetUrl = "https://www.pixiv.net/ajax/user/" + platformArtist.getIdInPlatform() + "/profile/all";

        String profileRespJson = asyncHttpUtil.sendGetRequest(profileGetUrl);

        // 处理
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(profileRespJson, Map.class);
        Map body = (Map) map.get("body");
        Map illusts = (Map) body.get("illusts");
        Set<String> illustsSet = illusts.keySet();
        // 最终的作品id列表
        List<String> illustsIdList = new ArrayList<>(illustsSet);
        logger.info(String.format("获取成功，共%d条结果", illustsIdList.size()));

        int step = 0;
        String url = "https://www.pixiv.net/ajax/user/" + platformArtist.getIdInPlatform() + "/profile/illusts?";
        List<String> workDetailGetUrls = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer(url);                  // 就用这个，毕竟多线程任务，怕出错
        for (String illustId : illustsIdList) {
            if (step == 9) {
                stringBuffer.append("work_category=illust&is_first_page=1");        // 这两个参数是必要的
                workDetailGetUrls.add(stringBuffer.toString());
                stringBuffer = new StringBuffer(url);                   // 重置
                step = 0;
            } else {
                stringBuffer.append("ids[]=").append(illustId).append("&");
            }
            step++;
        }

        // 通过这些地址请求获得响应结果
        String s = asyncHttpUtil.sendGetRequest(workDetailGetUrls.get(1));
        System.out.println(s);

        return fileList;
    }


}
