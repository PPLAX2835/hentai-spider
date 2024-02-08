package xyz.pplax.spider.spiders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.pplax.spider.utils.AsyncHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FurAffinitySpider {

    private static AsyncHttpUtil asyncHttpUtil;
    @Autowired
    public void setAsyncHttpUtil(AsyncHttpUtil myUtil) {
        asyncHttpUtil = myUtil;
    }

    private static String cookie;
    @Value("${pplax.spider.furaffinity.cookie}")
    public void setCookie(String myCookie){
        cookie = myCookie;
    }

    /**
     * 作品id的正则
     */
    private static final String regexForId = "sid-(\\d+)";
    private static Pattern patternForId = Pattern.compile(regexForId);

    /**
     * 作品文件url的正则
     */
    private static final String regexForUrl = "<div class=\"download\"><a href=\"(//[^\">]+)\">Download</a></div>";
    private static Pattern patternForUrl = Pattern.compile(regexForUrl);

    /**
     * 根据作者名获得其作品id列表
     * @param artistName
     * @return
     */
    public static List<String> getArtistIdList(String artistName) {
        List<String> ids = new ArrayList<>();

        Integer page = 1;
        while (true) {
            System.out.println(1);
            List<String> artistIdList = getArtistIdList(artistName, page);

            if (artistIdList.size() == 0) {
                break;
            }
            ids.addAll(artistIdList);

            page++;
        }

        return ids;
    }


    /**
     * 获得指定页的
     * @param artistName
     * @param page
     * @return
     */
    public static List<String> getArtistIdList(String artistName, Integer page) {
        List<String> ids = new ArrayList<>();

        final String targetUrl =  "https://www.furaffinity.net/gallery/" + artistName + (page == 1 ? "" : "/" + page) +"/?";
        System.out.println(targetUrl);
        // 设置cookie
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);

        // 获得html文本
        String responseString = asyncHttpUtil.sendGetRequest(targetUrl, headers);

        // 开始匹配
        Matcher matcher = patternForId.matcher(responseString);
        while (matcher.find()) {
            String extractedString = matcher.group(1);
            ids.add(extractedString);
        }

        System.out.println( "page" + page + ids);
        return ids;
    }

    /**
     * 获得作品文件url
     * @param sid
     * @return
     */
    public static String getFileUrl(String sid) {
        final String targetUrl =  "https://www.furaffinity.net/view/" + sid + '/';
        System.out.println(targetUrl);

        // 设置cookie
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);

        // 获得html文本
        String responseString = asyncHttpUtil.sendGetRequest(targetUrl, headers);

        // 开始匹配
        Matcher matcher = patternForUrl.matcher(responseString);
        while (matcher.find()) {
            return "https:" + matcher.group(1);
        }
        return null;
    }


}
