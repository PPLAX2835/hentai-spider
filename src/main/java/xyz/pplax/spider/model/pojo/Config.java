package xyz.pplax.spider.model.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * config
 * @author 
 */
@Data
public class Config implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 最大请求失败重试次数
     */
    private Integer maxRequestFailNum;

    /**
     * 文件存储的基本路径
     */
    private String basePath;

    /**
     * 是否启用代理
     */
    private Boolean proxyEnable;

    /**
     * 代理ip
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private Integer proxyPort;

    /**
     * user-agent头
     */
    private String userAgent;

    /**
     * 是否携带cookie，如果你想要furaffinity的限制内容，就要带上cookie
     */
    private Boolean furaffinityEnableCookie;

    /**
     * furaffinity的cookie
     */
    private String furaffinityCookie;

    /**
     * 是否携带cookie，如果你想要pixiv的限制内容，就要带上cookie
     */
    private Boolean pixivEnableCookie;

    /**
     * furaffinity的cookie
     */
    private String pixivCookie;


    private static final long serialVersionUID = 1L;
}